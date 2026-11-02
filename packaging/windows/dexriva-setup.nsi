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
!ifndef BRANDROOT
  !error "BRANDROOT missing"
!endif

Name "DEXRIVA 1.0.0"
OutFile "${OUTDIR}\DEXRIVA-1.0.0-setup.exe"
Unicode true
RequestExecutionLevel user
InstallDir "$LOCALAPPDATA\Programs\DEXRIVA"
InstallDirRegKey HKCU "Software\DEXRIVA" "InstallDir"
SetCompressor /SOLID lzma
SetCompressorDictSize 32
Icon "${ICONROOT}"
UninstallIcon "${ICONROOT}"
ShowInstDetails show
ShowUninstDetails show
BrandingText "DEXRIVA  •  Android Code Explorer"

; Original DEXRIVA install artwork, generated locally during packaging.
!define MUI_HEADERIMAGE
!define MUI_HEADERIMAGE_BITMAP "${BRANDROOT}\dexriva-header.bmp"
!define MUI_WELCOMEFINISHPAGE_BITMAP "${BRANDROOT}\dexriva-welcome.bmp"
!define MUI_UNWELCOMEFINISHPAGE_BITMAP "${BRANDROOT}\dexriva-welcome.bmp"
!define MUI_ABORTWARNING
!define MUI_ICON "${ICONROOT}"
!define MUI_UNICON "${ICONROOT}"

!define MUI_WELCOMEPAGE_TITLE "Welcome to DEXRIVA"
!define MUI_WELCOMEPAGE_TEXT "A modern workspace for exploring Android packages, bytecode, and resources.$\r$\n$\r$\nThis wizard installs DEXRIVA for the current Windows user. No administrator permissions are required."
!define MUI_LICENSEPAGE_CHECKBOX
!define MUI_LICENSEPAGE_CHECKBOX_TEXT "I accept the terms of the license agreement"
!define MUI_COMPONENTSPAGE_SMALLDESC
!define MUI_FINISHPAGE_RUN "$INSTDIR\DEXRIVA.exe"
!define MUI_FINISHPAGE_RUN_TEXT "Launch DEXRIVA"
!define MUI_FINISHPAGE_LINK ""
!define MUI_FINISHPAGE_TITLE "DEXRIVA is ready"
!define MUI_UNCONFIRMPAGE_TEXT_TOP "Remove DEXRIVA program files and shortcuts? Your personal projects will not be deleted."

!insertmacro MUI_PAGE_WELCOME
; Preserve original Apache-2.0 license; display agreement before installation.
!insertmacro MUI_PAGE_LICENSE "${APPROOT}\LICENSE"
!insertmacro MUI_PAGE_COMPONENTS
!insertmacro MUI_PAGE_DIRECTORY
!insertmacro MUI_PAGE_INSTFILES
!insertmacro MUI_PAGE_FINISH
!insertmacro MUI_UNPAGE_WELCOME
!insertmacro MUI_UNPAGE_CONFIRM
!insertmacro MUI_UNPAGE_INSTFILES
!insertmacro MUI_UNPAGE_FINISH
!insertmacro MUI_LANGUAGE "English"

Section "DEXRIVA application (required)" SecCore
  SectionIn RO
  SetShellVarContext current
  SetOutPath "$INSTDIR"
  File /r "${APPROOT}\*"

  ; Program and uninstall metadata only; no unrelated system settings.
  WriteRegStr HKCU "Software\DEXRIVA" "InstallDir" "$INSTDIR"
  WriteRegStr HKCU "Software\Microsoft\Windows\CurrentVersion\Uninstall\DEXRIVA" "DisplayName" "DEXRIVA 1.0.0"
  WriteRegStr HKCU "Software\Microsoft\Windows\CurrentVersion\Uninstall\DEXRIVA" "DisplayVersion" "1.0.0"
  WriteRegStr HKCU "Software\Microsoft\Windows\CurrentVersion\Uninstall\DEXRIVA" "Publisher" "DEXRIVA contributors"
  WriteRegStr HKCU "Software\Microsoft\Windows\CurrentVersion\Uninstall\DEXRIVA" "DisplayIcon" "$INSTDIR\DEXRIVA.exe"
  WriteRegStr HKCU "Software\Microsoft\Windows\CurrentVersion\Uninstall\DEXRIVA" "InstallLocation" "$INSTDIR"
  WriteRegStr HKCU "Software\Microsoft\Windows\CurrentVersion\Uninstall\DEXRIVA" "UninstallString" '"$INSTDIR\Uninstall.exe"'
  WriteRegDWORD HKCU "Software\Microsoft\Windows\CurrentVersion\Uninstall\DEXRIVA" "NoModify" 1
  WriteRegDWORD HKCU "Software\Microsoft\Windows\CurrentVersion\Uninstall\DEXRIVA" "NoRepair" 1
  WriteUninstaller "$INSTDIR\Uninstall.exe"
SectionEnd

Section "Start menu shortcut" SecMenu
  SetShellVarContext current
  CreateDirectory "$SMPROGRAMS\DEXRIVA"
  CreateShortcut "$SMPROGRAMS\DEXRIVA\DEXRIVA.lnk" "$INSTDIR\DEXRIVA.exe"
SectionEnd

Section /o "Desktop shortcut" SecDesktop
  SetShellVarContext current
  CreateShortcut "$DESKTOP\DEXRIVA.lnk" "$INSTDIR\DEXRIVA.exe"
SectionEnd

!insertmacro MUI_FUNCTION_DESCRIPTION_BEGIN
  !insertmacro MUI_DESCRIPTION_TEXT ${SecCore} "Install the DEXRIVA desktop application, bundled Java runtime and required legal notices."
  !insertmacro MUI_DESCRIPTION_TEXT ${SecMenu} "Add a DEXRIVA shortcut to your Start menu."
  !insertmacro MUI_DESCRIPTION_TEXT ${SecDesktop} "Create an optional DEXRIVA desktop shortcut."
!insertmacro MUI_FUNCTION_DESCRIPTION_END

Section "Uninstall"
  SetShellVarContext current
  Delete "$DESKTOP\DEXRIVA.lnk"
  Delete "$SMPROGRAMS\DEXRIVA\DEXRIVA.lnk"
  RMDir "$SMPROGRAMS\DEXRIVA"
  DeleteRegKey HKCU "Software\Microsoft\Windows\CurrentVersion\Uninstall\DEXRIVA"
  DeleteRegKey HKCU "Software\DEXRIVA"

  ; Do not delete user workspace files, settings, or any external project folders.
  RMDir /r "$INSTDIR"
SectionEnd
