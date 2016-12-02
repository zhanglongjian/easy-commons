@echo off
echo [INFO] 生成项目模板...
cd %~dp0
cd ..
call mvn archetype:create-from-project
cd bin
pause