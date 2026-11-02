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
Name "DEXRIVA 1.0.0"
OutFile "${OUTDIR}\DEXRIVA-1.0.0-setup.exe"
Unicode true
RequestExecutionLevel user
InstallDir "$LOCALAPPDATA\Programs\DEXRIVA"
InstallDirRegKey HKCU "Software\DEXRIVA" "InstallDir"
SetCompressor /SOLID lzma
Icon "${ICONROOT}"
UninstallIcon "${ICONROOT}"
ShowInstDetails show
ShowUninstDetails show
!define MUI_ABORTWARNING
!insertmacro MUI_PAGE_WELCOME
!insertmacro MUI_PAGE_DIRECTORY
!insertmacro MUI_PAGE_INSTFILES
!insertmacro MUI_PAGE_FINISH
!insertmacro MUI_UNPAGE_CONFIRM
!insertmacro MUI_UNPAGE_INSTFILES
!insertmacro MUI_LANGUAGE "English"

Section "Install"
  SetOutPath "$INSTDIR"
  File /r "${APPROOT}\*"
  CreateDirectory "$SMPROGRAMS\DEXRIVA"
  CreateShortcut "$SMPROGRAMS\DEXRIVA\DEXRIVA.lnk" "$INSTDIR\DEXRIVA.exe"
  CreateShortcut "$DESKTOP\DEXRIVA.lnk" "$INSTDIR\DEXRIVA.exe"
  WriteRegStr HKCU "Software\DEXRIVA" "InstallDir" "$INSTDIR"
  WriteRegStr HKCU "Software\Microsoft\Windows\CurrentVersion\Uninstall\DEXRIVA" "DisplayName" "DEXRIVA 1.0.0"
  WriteRegStr HKCU "Software\Microsoft\Windows\CurrentVersion\Uninstall\DEXRIVA" "DisplayVersion" "1.0.0"
  WriteRegStr HKCU "Software\Microsoft\Windows\CurrentVersion\Uninstall\DEXRIVA" "Publisher" "DEXRIVA contributors"
  WriteRegStr HKCU "Software\Microsoft\Windows\CurrentVersion\Uninstall\DEXRIVA" "DisplayIcon" "$INSTDIR\DEXRIVA.exe"
  WriteRegStr HKCU "Software\Microsoft\Windows\CurrentVersion\Uninstall\DEXRIVA" "UninstallString" '"$INSTDIR\Uninstall.exe"'
  WriteRegDWORD HKCU "Software\Microsoft\Windows\CurrentVersion\Uninstall\DEXRIVA" "NoModify" 1
  WriteRegDWORD HKCU "Software\Microsoft\Windows\CurrentVersion\Uninstall\DEXRIVA" "NoRepair" 1
  WriteUninstaller "$INSTDIR\Uninstall.exe"
SectionEnd

Section "Uninstall"
  Delete "$DESKTOP\DEXRIVA.lnk"
  Delete "$SMPROGRAMS\DEXRIVA\DEXRIVA.lnk"
  RMDir "$SMPROGRAMS\DEXRIVA"
  DeleteRegKey HKCU "Software\Microsoft\Windows\CurrentVersion\Uninstall\DEXRIVA"
  DeleteRegKey HKCU "Software\DEXRIVA"
  RMDir /r "$INSTDIR"
SectionEnd
