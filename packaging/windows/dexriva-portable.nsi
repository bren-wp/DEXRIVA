!include "MUI2.nsh"
!ifndef APPROOT
  !error "APPROOT missing"
!endif
!ifndef OUTDIR
  !error "OUTDIR missing"
!endif
!ifndef ICONROOT
  !error "ICONROOT missing"
!endif
Name "DEXRIVA Portable 1.0.0"
OutFile "${OUTDIR}\DEXRIVA-1.0.0-portable.exe"
Unicode true
RequestExecutionLevel user
SetCompressor /SOLID lzma
Icon "${ICONROOT}"
SilentInstall normal
Page instfiles
AutoCloseWindow true
Section "Portable launch"
  ; Extract next to the launcher, never use Program Files, registry, or an administrator account.
  IfFileExists "$EXEDIR\DEXRIVA-Portable\DEXRIVA.exe" launch
  SetOutPath "$EXEDIR\DEXRIVA-Portable"
  ClearErrors
  File /r "${APPROOT}\*"
  IfErrors 0 launch
  MessageBox MB_ICONSTOP "Unable to extract beside the portable launcher. Move it to a writable folder."
  Abort
launch:
  IfSilent done
  Exec '"$EXEDIR\DEXRIVA-Portable\DEXRIVA.exe"'
done:
SectionEnd
