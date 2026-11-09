#!/usr/bin/env bash
set -euo pipefail

name="TT_MODRINTH_TOKEN"
config_dir="${XDG_CONFIG_HOME:-$HOME/.config}/totemtweaks"
env_file="$config_dir/publishing.env"
token_url="https://modrinth.com/settings/pats"

check_token() {
    if ! command -v curl >/dev/null 2>&1; then
        printf '000'
        return
    fi
    curl -s -o /dev/null -w '%{http_code}' --max-time 15 -A "Totem Tweaks publishing setup" \
        -H "Authorization: $1" "https://api.modrinth.com/v2/user" || true
}

confirm() {
    local answer
    printf '%s ' "$1"
    IFS= read -r answer || exit 1
    case "$(printf '%s' "${answer:-$2}" | tr '[:upper:]' '[:lower:]')" in
        y | yes) return 0 ;;
        *) return 1 ;;
    esac
}

open_url() {
    if [ "$(uname -s)" = Darwin ]; then
        open "$1" >/dev/null 2>&1 || true
    elif command -v xdg-open >/dev/null 2>&1; then
        xdg-open "$1" >/dev/null 2>&1 &
    fi
    printf 'If no browser opened, go to %s\n' "$1"
}

sh_quote() {
    printf "'%s'" "$(printf '%s' "$1" | sed "s/'/'\\\\''/g")"
}

fish_quote() {
    printf "'%s'" "$(printf '%s' "$1" | sed -e 's/\\/\\\\/g' -e "s/'/\\\\'/g")"
}

case "$(basename "${SHELL:-sh}")" in
    zsh) rc="${ZDOTDIR:-$HOME}/.zshrc" ;;
    # macOS opens every terminal as a login shell, which reads .bash_profile and skips .bashrc.
    bash) if [ "$(uname -s)" = Darwin ]; then rc="$HOME/.bash_profile"; else rc="$HOME/.bashrc"; fi ;;
    fish) rc="" ;;
    *) rc="$HOME/.profile" ;;
esac
# fish cannot read the export syntax of the shared file, so it gets its own copy.
loaded_by=${rc:-"${XDG_CONFIG_HOME:-$HOME/.config}/fish/conf.d/totemtweaks-publishing.fish"}

cat <<EOF
Totem Tweaks publishing setup

./gradlew publishMods needs a Modrinth personal access token in $name. This checks the token against Modrinth and
stores it for new terminals. The pasted value is hidden.

Create a PAT with the Create versions, Read versions, Write versions and Read user data scopes. Read user data is
only used to check the token.
EOF

if [ -f "$env_file" ]; then
    # shellcheck source=/dev/null
    . "$env_file"
fi
if [ -n "${!name-}" ] && ! confirm "$name is already set. Replace it? [y/N]" n; then
    exit 0
fi
if confirm "Open the page to create one in your browser? [Y/n]" y; then
    open_url "$token_url"
fi

while true; do
    printf 'Paste it, or press Enter to stop: '
    IFS= read -rs token || exit 1
    printf '\n'
    [ -z "$token" ] && exit 0

    case "$(check_token "$token")" in
        200) printf 'Accepted.\n'; break ;;
        000) printf 'Could not reach Modrinth to check it, keeping it anyway.\n'; break ;;
        *) printf 'Rejected by Modrinth.\n' ;;
    esac
done

umask 077
mkdir -p "$config_dir"
printf '# Written by scripts/setup-publishing.sh\nexport %s=%s\n' "$name" "$(sh_quote "$token")" >"$env_file"
chmod 600 "$env_file"

if [ -n "$rc" ]; then
    if ! grep -qF "$env_file" "$rc" 2>/dev/null; then
        printf '\n# Publishing token for Totem Tweaks\n%s\n' "[ -f \"$env_file\" ] && . \"$env_file\"" >>"$rc"
    fi
else
    mkdir -p "$(dirname "$loaded_by")"
    printf 'set -gx %s %s\n' "$name" "$(fish_quote "$token")" >"$loaded_by"
    chmod 600 "$loaded_by"
fi

printf 'Saved to %s, loaded by %s.\n' "$env_file" "$loaded_by"
printf 'Open a new terminal, and restart your IDE, before publishing.\n'
