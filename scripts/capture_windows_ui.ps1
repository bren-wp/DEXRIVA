param(
    [Parameter(Mandatory = $true)][string]$Launcher,
    [Parameter(Mandatory = $true)][string]$Output
)
$ErrorActionPreference = 'Stop'
Add-Type -AssemblyName System.Drawing
Add-Type @'
using System;
using System.Runtime.InteropServices;
public static class DexriveCaptureNative {
    [StructLayout(LayoutKind.Sequential)]
    public struct Rect { public int Left; public int Top; public int Right; public int Bottom; }
    [DllImport("user32.dll")] public static extern bool GetWindowRect(IntPtr handle, out Rect rect);
    [DllImport("user32.dll")] public static extern bool ShowWindow(IntPtr handle, int command);
    [DllImport("user32.dll")] public static extern bool SetForegroundWindow(IntPtr handle);
}
'@
$exe = (Resolve-Path $Launcher).Path
New-Item -ItemType Directory -Path (Split-Path $Output -Parent) -Force | Out-Null
$process = Start-Process -FilePath $exe -PassThru
try {
    $found = $null
    for ($i = 0; $i -lt 90; $i++) {
        Start-Sleep -Seconds 1
        $matches = @(Get-Process | Where-Object {
            $_.MainWindowHandle -ne [IntPtr]::Zero -and
            $_.MainWindowTitle -match 'DEXRIVE' -and
            ($_.ProcessName -match 'DEXRIVE|java|javaw')
        })
        if ($matches.Count -gt 0) {
            $found = $matches[0]
            break
        }
        if ($process.HasExited) { throw "DEXRIVE exited before opening its GUI: $($process.ExitCode)" }
    }
    if ($null -eq $found) { throw 'No real DEXRIVE main window was found; refusing to use a mock screenshot.' }
    [DexriveCaptureNative]::ShowWindow($found.MainWindowHandle, 9) | Out-Null
    [DexriveCaptureNative]::SetForegroundWindow($found.MainWindowHandle) | Out-Null
    Start-Sleep -Seconds 8
    $rect = New-Object DexriveCaptureNative+Rect
    if (-not [DexriveCaptureNative]::GetWindowRect($found.MainWindowHandle,[ref]$rect)) {
        throw 'Could not read actual window bounds.'
    }
    $width = $rect.Right - $rect.Left
    $height = $rect.Bottom - $rect.Top
    if ($width -lt 500 -or $height -lt 350) { throw "Invalid capture bounds: $width x $height" }
    $bitmap = New-Object System.Drawing.Bitmap($width, $height)
    try {
        $graphics = [System.Drawing.Graphics]::FromImage($bitmap)
        try {
            $graphics.CopyFromScreen($rect.Left,$rect.Top,0,0,$bitmap.Size)
        } finally { $graphics.Dispose() }
        $colors = [System.Collections.Generic.HashSet[int]]::new()
        for ($x = 0; $x -lt $width; $x += [Math]::Max(1,[int]($width / 22))) {
            for ($y = 0; $y -lt $height; $y += [Math]::Max(1,[int]($height / 16))) {
                [void]$colors.Add($bitmap.GetPixel($x,$y).ToArgb())
            }
        }
        if ($colors.Count -lt 12) { throw "Actual desktop framebuffer looks blank ($($colors.Count) distinct sample colors)." }
        $bitmap.Save((Join-Path (Get-Location) $Output),[System.Drawing.Imaging.ImageFormat]::Png)
        Write-Host "Saved genuine Windows GUI capture ($width x $height; $($colors.Count) colors): $Output"
    } finally { $bitmap.Dispose() }
} finally {
    if (-not $process.HasExited) { Stop-Process -Id $process.Id -Force -ErrorAction SilentlyContinue }
}
