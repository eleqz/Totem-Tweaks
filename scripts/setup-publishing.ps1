#Requires -Version 5.1
$ErrorActionPreference = 'Stop'

# Windows PowerShell 5.1 can still default to TLS versions that Modrinth refuses.
[Net.ServicePointManager]::SecurityProtocol = [Net.ServicePointManager]::SecurityProtocol -bor [Net.SecurityProtocolType]::Tls12

$name = 'TT_MODRINTH_TOKEN'
$tokenUrl = 'https://modrinth.com/settings/pats'

function Test-Token([string]$Token) {
    try {
        (Invoke-WebRequest -Uri 'https://api.modrinth.com/v2/user' -Headers @{ Authorization = $Token } `
            -UserAgent 'Totem Tweaks publishing setup' -UseBasicParsing -TimeoutSec 15).StatusCode
    } catch {
        $response = $_.Exception.Response
        if ($response) { [int]$response.StatusCode } else { 0 }
    }
}

function Confirm-Choice([string]$Prompt, [bool]$Default) {
    $answer = Read-Host -Prompt $Prompt
    if (-not $answer) { return $Default }
    return $answer -match '^(y|yes)$'
}

function Read-Hidden([string]$Prompt) {
    $secure = Read-Host -Prompt $Prompt -AsSecureString
    $bstr = [Runtime.InteropServices.Marshal]::SecureStringToBSTR($secure)
    try { [Runtime.InteropServices.Marshal]::PtrToStringBSTR($bstr) } finally { [Runtime.InteropServices.Marshal]::ZeroFreeBSTR($bstr) }
}

Write-Host 'Totem Tweaks publishing setup'
Write-Host ''
Write-Host ".\gradlew publishMods needs a Modrinth personal access token in $name. This checks the token against Modrinth"
Write-Host 'and stores it as an environment variable for your Windows account. The pasted value is hidden.'
Write-Host ''
Write-Host 'Create a PAT with the Create versions, Read versions, Write versions and Read user data scopes. Read user data'
Write-Host 'is only used to check the token.'

if ([Environment]::GetEnvironmentVariable($name, 'User') -and -not (Confirm-Choice "$name is already set. Replace it? [y/N]" $false)) {
    exit 0
}
if (Confirm-Choice 'Open the page to create one in your browser? [Y/n]' $true) {
    Start-Process $tokenUrl
    Write-Host "If no browser opened, go to $tokenUrl"
}

while ($true) {
    $token = Read-Hidden 'Paste it, or press Enter to stop'
    if (-not $token) { exit 0 }

    $result = Test-Token $token
    if ($result -eq 200) { Write-Host 'Accepted.'; break }
    if ($result -eq 0) { Write-Host 'Could not reach Modrinth to check it, keeping it anyway.'; break }
    Write-Host "Rejected by Modrinth ($result)."
}

[Environment]::SetEnvironmentVariable($name, $token, 'User')
Write-Host 'Saved as a user environment variable.'
Write-Host 'Open a new terminal, and restart your IDE, before publishing.'
