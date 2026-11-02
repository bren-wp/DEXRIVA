!include "FileFunc.nsh"
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
BrandingText "DEXRIVA Portable"

; No wizard, no registry entries, no shortcuts, no administrative permissions.
; The portable EXE contains the app/runtime and silently extracts beside itself.
SilentInstall silent
AutoCloseWindow true
!insertmacro GetParameters
!insertmacro GetOptions

Section "Portable launch"
  IfFileExists "$EXEDIR\DEXRIVA-Portable\DEXRIVA.exe" ready
  SetOutPath "$EXEDIR\DEXRIVA-Portable"
  ClearErrors
  File /r "${APPROOT}\*"
  IfErrors 0 ready
  MessageBox MB_ICONSTOP "Unable to extract DEXRIVA beside the portable executable. Choose a writable folder."
  Abort
ready:
  ; /S is used only by CI and enterprise silent extraction.
  ${GetParameters} $0
  ${GetOptions} $0 "/S" $1
  IfErrors normal
  Goto done
normal:
  Exec '"$EXEDIR\DEXRIVA-Portable\DEXRIVA.exe"'
done:
SectionEnd
