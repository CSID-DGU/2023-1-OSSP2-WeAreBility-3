# Install script for directory: C:/Users/wjdgu/Documents/2023-1-OSSP2-WeAreBility-3/2023-1-OSSP2-WeAreBility-3/frontend/naemansan/windows

# Set the install prefix
if(NOT DEFINED CMAKE_INSTALL_PREFIX)
  set(CMAKE_INSTALL_PREFIX "$<TARGET_FILE_DIR:naemansan>")
endif()
string(REGEX REPLACE "/$" "" CMAKE_INSTALL_PREFIX "${CMAKE_INSTALL_PREFIX}")

# Set the install configuration name.
if(NOT DEFINED CMAKE_INSTALL_CONFIG_NAME)
  if(BUILD_TYPE)
    string(REGEX REPLACE "^[^A-Za-z0-9_]+" ""
           CMAKE_INSTALL_CONFIG_NAME "${BUILD_TYPE}")
  else()
    set(CMAKE_INSTALL_CONFIG_NAME "Release")
  endif()
  message(STATUS "Install configuration: \"${CMAKE_INSTALL_CONFIG_NAME}\"")
endif()

# Set the component getting installed.
if(NOT CMAKE_INSTALL_COMPONENT)
  if(COMPONENT)
    message(STATUS "Install component: \"${COMPONENT}\"")
    set(CMAKE_INSTALL_COMPONENT "${COMPONENT}")
  else()
    set(CMAKE_INSTALL_COMPONENT)
  endif()
endif()

# Is this installation the result of a crosscompile?
if(NOT DEFINED CMAKE_CROSSCOMPILING)
  set(CMAKE_CROSSCOMPILING "FALSE")
endif()

if(NOT CMAKE_INSTALL_LOCAL_ONLY)
  # Include the install script for the subdirectory.
  include("C:/Users/wjdgu/Documents/2023-1-OSSP2-WeAreBility-3/2023-1-OSSP2-WeAreBility-3/frontend/naemansan/build/windows/flutter/cmake_install.cmake")
endif()

if(NOT CMAKE_INSTALL_LOCAL_ONLY)
  # Include the install script for the subdirectory.
  include("C:/Users/wjdgu/Documents/2023-1-OSSP2-WeAreBility-3/2023-1-OSSP2-WeAreBility-3/frontend/naemansan/build/windows/runner/cmake_install.cmake")
endif()

if(NOT CMAKE_INSTALL_LOCAL_ONLY)
  # Include the install script for the subdirectory.
  include("C:/Users/wjdgu/Documents/2023-1-OSSP2-WeAreBility-3/2023-1-OSSP2-WeAreBility-3/frontend/naemansan/build/windows/plugins/geolocator_windows/cmake_install.cmake")
endif()

if(NOT CMAKE_INSTALL_LOCAL_ONLY)
  # Include the install script for the subdirectory.
  include("C:/Users/wjdgu/Documents/2023-1-OSSP2-WeAreBility-3/2023-1-OSSP2-WeAreBility-3/frontend/naemansan/build/windows/plugins/permission_handler_windows/cmake_install.cmake")
endif()

if(CMAKE_INSTALL_COMPONENT STREQUAL "Runtime" OR NOT CMAKE_INSTALL_COMPONENT)
  if(CMAKE_INSTALL_CONFIG_NAME MATCHES "^([Dd][Ee][Bb][Uu][Gg])$")
    list(APPEND CMAKE_ABSOLUTE_DESTINATION_FILES
     "C:/Users/wjdgu/Documents/2023-1-OSSP2-WeAreBility-3/2023-1-OSSP2-WeAreBility-3/frontend/naemansan/build/windows/runner/Debug/naemansan.exe")
    if(CMAKE_WARN_ON_ABSOLUTE_INSTALL_DESTINATION)
      message(WARNING "ABSOLUTE path INSTALL DESTINATION : ${CMAKE_ABSOLUTE_DESTINATION_FILES}")
    endif()
    if(CMAKE_ERROR_ON_ABSOLUTE_INSTALL_DESTINATION)
      message(FATAL_ERROR "ABSOLUTE path INSTALL DESTINATION forbidden (by caller): ${CMAKE_ABSOLUTE_DESTINATION_FILES}")
    endif()
    file(INSTALL DESTINATION "C:/Users/wjdgu/Documents/2023-1-OSSP2-WeAreBility-3/2023-1-OSSP2-WeAreBility-3/frontend/naemansan/build/windows/runner/Debug" TYPE EXECUTABLE FILES "C:/Users/wjdgu/Documents/2023-1-OSSP2-WeAreBility-3/2023-1-OSSP2-WeAreBility-3/frontend/naemansan/build/windows/runner/Debug/naemansan.exe")
  elseif(CMAKE_INSTALL_CONFIG_NAME MATCHES "^([Pp][Rr][Oo][Ff][Ii][Ll][Ee])$")
    list(APPEND CMAKE_ABSOLUTE_DESTINATION_FILES
     "C:/Users/wjdgu/Documents/2023-1-OSSP2-WeAreBility-3/2023-1-OSSP2-WeAreBility-3/frontend/naemansan/build/windows/runner/Profile/naemansan.exe")
    if(CMAKE_WARN_ON_ABSOLUTE_INSTALL_DESTINATION)
      message(WARNING "ABSOLUTE path INSTALL DESTINATION : ${CMAKE_ABSOLUTE_DESTINATION_FILES}")
    endif()
    if(CMAKE_ERROR_ON_ABSOLUTE_INSTALL_DESTINATION)
      message(FATAL_ERROR "ABSOLUTE path INSTALL DESTINATION forbidden (by caller): ${CMAKE_ABSOLUTE_DESTINATION_FILES}")
    endif()
    file(INSTALL DESTINATION "C:/Users/wjdgu/Documents/2023-1-OSSP2-WeAreBility-3/2023-1-OSSP2-WeAreBility-3/frontend/naemansan/build/windows/runner/Profile" TYPE EXECUTABLE FILES "C:/Users/wjdgu/Documents/2023-1-OSSP2-WeAreBility-3/2023-1-OSSP2-WeAreBility-3/frontend/naemansan/build/windows/runner/Profile/naemansan.exe")
  elseif(CMAKE_INSTALL_CONFIG_NAME MATCHES "^([Rr][Ee][Ll][Ee][Aa][Ss][Ee])$")
    list(APPEND CMAKE_ABSOLUTE_DESTINATION_FILES
     "C:/Users/wjdgu/Documents/2023-1-OSSP2-WeAreBility-3/2023-1-OSSP2-WeAreBility-3/frontend/naemansan/build/windows/runner/Release/naemansan.exe")
    if(CMAKE_WARN_ON_ABSOLUTE_INSTALL_DESTINATION)
      message(WARNING "ABSOLUTE path INSTALL DESTINATION : ${CMAKE_ABSOLUTE_DESTINATION_FILES}")
    endif()
    if(CMAKE_ERROR_ON_ABSOLUTE_INSTALL_DESTINATION)
      message(FATAL_ERROR "ABSOLUTE path INSTALL DESTINATION forbidden (by caller): ${CMAKE_ABSOLUTE_DESTINATION_FILES}")
    endif()
    file(INSTALL DESTINATION "C:/Users/wjdgu/Documents/2023-1-OSSP2-WeAreBility-3/2023-1-OSSP2-WeAreBility-3/frontend/naemansan/build/windows/runner/Release" TYPE EXECUTABLE FILES "C:/Users/wjdgu/Documents/2023-1-OSSP2-WeAreBility-3/2023-1-OSSP2-WeAreBility-3/frontend/naemansan/build/windows/runner/Release/naemansan.exe")
  endif()
endif()

if(CMAKE_INSTALL_COMPONENT STREQUAL "Runtime" OR NOT CMAKE_INSTALL_COMPONENT)
  if(CMAKE_INSTALL_CONFIG_NAME MATCHES "^([Dd][Ee][Bb][Uu][Gg])$")
    list(APPEND CMAKE_ABSOLUTE_DESTINATION_FILES
     "C:/Users/wjdgu/Documents/2023-1-OSSP2-WeAreBility-3/2023-1-OSSP2-WeAreBility-3/frontend/naemansan/build/windows/runner/Debug/data/icudtl.dat")
    if(CMAKE_WARN_ON_ABSOLUTE_INSTALL_DESTINATION)
      message(WARNING "ABSOLUTE path INSTALL DESTINATION : ${CMAKE_ABSOLUTE_DESTINATION_FILES}")
    endif()
    if(CMAKE_ERROR_ON_ABSOLUTE_INSTALL_DESTINATION)
      message(FATAL_ERROR "ABSOLUTE path INSTALL DESTINATION forbidden (by caller): ${CMAKE_ABSOLUTE_DESTINATION_FILES}")
    endif()
    file(INSTALL DESTINATION "C:/Users/wjdgu/Documents/2023-1-OSSP2-WeAreBility-3/2023-1-OSSP2-WeAreBility-3/frontend/naemansan/build/windows/runner/Debug/data" TYPE FILE FILES "C:/Users/wjdgu/Documents/2023-1-OSSP2-WeAreBility-3/2023-1-OSSP2-WeAreBility-3/frontend/naemansan/windows/flutter/ephemeral/icudtl.dat")
  elseif(CMAKE_INSTALL_CONFIG_NAME MATCHES "^([Pp][Rr][Oo][Ff][Ii][Ll][Ee])$")
    list(APPEND CMAKE_ABSOLUTE_DESTINATION_FILES
     "C:/Users/wjdgu/Documents/2023-1-OSSP2-WeAreBility-3/2023-1-OSSP2-WeAreBility-3/frontend/naemansan/build/windows/runner/Profile/data/icudtl.dat")
    if(CMAKE_WARN_ON_ABSOLUTE_INSTALL_DESTINATION)
      message(WARNING "ABSOLUTE path INSTALL DESTINATION : ${CMAKE_ABSOLUTE_DESTINATION_FILES}")
    endif()
    if(CMAKE_ERROR_ON_ABSOLUTE_INSTALL_DESTINATION)
      message(FATAL_ERROR "ABSOLUTE path INSTALL DESTINATION forbidden (by caller): ${CMAKE_ABSOLUTE_DESTINATION_FILES}")
    endif()
    file(INSTALL DESTINATION "C:/Users/wjdgu/Documents/2023-1-OSSP2-WeAreBility-3/2023-1-OSSP2-WeAreBility-3/frontend/naemansan/build/windows/runner/Profile/data" TYPE FILE FILES "C:/Users/wjdgu/Documents/2023-1-OSSP2-WeAreBility-3/2023-1-OSSP2-WeAreBility-3/frontend/naemansan/windows/flutter/ephemeral/icudtl.dat")
  elseif(CMAKE_INSTALL_CONFIG_NAME MATCHES "^([Rr][Ee][Ll][Ee][Aa][Ss][Ee])$")
    list(APPEND CMAKE_ABSOLUTE_DESTINATION_FILES
     "C:/Users/wjdgu/Documents/2023-1-OSSP2-WeAreBility-3/2023-1-OSSP2-WeAreBility-3/frontend/naemansan/build/windows/runner/Release/data/icudtl.dat")
    if(CMAKE_WARN_ON_ABSOLUTE_INSTALL_DESTINATION)
      message(WARNING "ABSOLUTE path INSTALL DESTINATION : ${CMAKE_ABSOLUTE_DESTINATION_FILES}")
    endif()
    if(CMAKE_ERROR_ON_ABSOLUTE_INSTALL_DESTINATION)
      message(FATAL_ERROR "ABSOLUTE path INSTALL DESTINATION forbidden (by caller): ${CMAKE_ABSOLUTE_DESTINATION_FILES}")
    endif()
    file(INSTALL DESTINATION "C:/Users/wjdgu/Documents/2023-1-OSSP2-WeAreBility-3/2023-1-OSSP2-WeAreBility-3/frontend/naemansan/build/windows/runner/Release/data" TYPE FILE FILES "C:/Users/wjdgu/Documents/2023-1-OSSP2-WeAreBility-3/2023-1-OSSP2-WeAreBility-3/frontend/naemansan/windows/flutter/ephemeral/icudtl.dat")
  endif()
endif()

if(CMAKE_INSTALL_COMPONENT STREQUAL "Runtime" OR NOT CMAKE_INSTALL_COMPONENT)
  if(CMAKE_INSTALL_CONFIG_NAME MATCHES "^([Dd][Ee][Bb][Uu][Gg])$")
    list(APPEND CMAKE_ABSOLUTE_DESTINATION_FILES
     "C:/Users/wjdgu/Documents/2023-1-OSSP2-WeAreBility-3/2023-1-OSSP2-WeAreBility-3/frontend/naemansan/build/windows/runner/Debug/flutter_windows.dll")
    if(CMAKE_WARN_ON_ABSOLUTE_INSTALL_DESTINATION)
      message(WARNING "ABSOLUTE path INSTALL DESTINATION : ${CMAKE_ABSOLUTE_DESTINATION_FILES}")
    endif()
    if(CMAKE_ERROR_ON_ABSOLUTE_INSTALL_DESTINATION)
      message(FATAL_ERROR "ABSOLUTE path INSTALL DESTINATION forbidden (by caller): ${CMAKE_ABSOLUTE_DESTINATION_FILES}")
    endif()
    file(INSTALL DESTINATION "C:/Users/wjdgu/Documents/2023-1-OSSP2-WeAreBility-3/2023-1-OSSP2-WeAreBility-3/frontend/naemansan/build/windows/runner/Debug" TYPE FILE FILES "C:/Users/wjdgu/Documents/2023-1-OSSP2-WeAreBility-3/2023-1-OSSP2-WeAreBility-3/frontend/naemansan/windows/flutter/ephemeral/flutter_windows.dll")
  elseif(CMAKE_INSTALL_CONFIG_NAME MATCHES "^([Pp][Rr][Oo][Ff][Ii][Ll][Ee])$")
    list(APPEND CMAKE_ABSOLUTE_DESTINATION_FILES
     "C:/Users/wjdgu/Documents/2023-1-OSSP2-WeAreBility-3/2023-1-OSSP2-WeAreBility-3/frontend/naemansan/build/windows/runner/Profile/flutter_windows.dll")
    if(CMAKE_WARN_ON_ABSOLUTE_INSTALL_DESTINATION)
      message(WARNING "ABSOLUTE path INSTALL DESTINATION : ${CMAKE_ABSOLUTE_DESTINATION_FILES}")
    endif()
    if(CMAKE_ERROR_ON_ABSOLUTE_INSTALL_DESTINATION)
      message(FATAL_ERROR "ABSOLUTE path INSTALL DESTINATION forbidden (by caller): ${CMAKE_ABSOLUTE_DESTINATION_FILES}")
    endif()
    file(INSTALL DESTINATION "C:/Users/wjdgu/Documents/2023-1-OSSP2-WeAreBility-3/2023-1-OSSP2-WeAreBility-3/frontend/naemansan/build/windows/runner/Profile" TYPE FILE FILES "C:/Users/wjdgu/Documents/2023-1-OSSP2-WeAreBility-3/2023-1-OSSP2-WeAreBility-3/frontend/naemansan/windows/flutter/ephemeral/flutter_windows.dll")
  elseif(CMAKE_INSTALL_CONFIG_NAME MATCHES "^([Rr][Ee][Ll][Ee][Aa][Ss][Ee])$")
    list(APPEND CMAKE_ABSOLUTE_DESTINATION_FILES
     "C:/Users/wjdgu/Documents/2023-1-OSSP2-WeAreBility-3/2023-1-OSSP2-WeAreBility-3/frontend/naemansan/build/windows/runner/Release/flutter_windows.dll")
    if(CMAKE_WARN_ON_ABSOLUTE_INSTALL_DESTINATION)
      message(WARNING "ABSOLUTE path INSTALL DESTINATION : ${CMAKE_ABSOLUTE_DESTINATION_FILES}")
    endif()
    if(CMAKE_ERROR_ON_ABSOLUTE_INSTALL_DESTINATION)
      message(FATAL_ERROR "ABSOLUTE path INSTALL DESTINATION forbidden (by caller): ${CMAKE_ABSOLUTE_DESTINATION_FILES}")
    endif()
    file(INSTALL DESTINATION "C:/Users/wjdgu/Documents/2023-1-OSSP2-WeAreBility-3/2023-1-OSSP2-WeAreBility-3/frontend/naemansan/build/windows/runner/Release" TYPE FILE FILES "C:/Users/wjdgu/Documents/2023-1-OSSP2-WeAreBility-3/2023-1-OSSP2-WeAreBility-3/frontend/naemansan/windows/flutter/ephemeral/flutter_windows.dll")
  endif()
endif()

if(CMAKE_INSTALL_COMPONENT STREQUAL "Runtime" OR NOT CMAKE_INSTALL_COMPONENT)
  if(CMAKE_INSTALL_CONFIG_NAME MATCHES "^([Dd][Ee][Bb][Uu][Gg])$")
    list(APPEND CMAKE_ABSOLUTE_DESTINATION_FILES
     "C:/Users/wjdgu/Documents/2023-1-OSSP2-WeAreBility-3/2023-1-OSSP2-WeAreBility-3/frontend/naemansan/build/windows/runner/Debug/geolocator_windows_plugin.dll;C:/Users/wjdgu/Documents/2023-1-OSSP2-WeAreBility-3/2023-1-OSSP2-WeAreBility-3/frontend/naemansan/build/windows/runner/Debug/permission_handler_windows_plugin.dll")
    if(CMAKE_WARN_ON_ABSOLUTE_INSTALL_DESTINATION)
      message(WARNING "ABSOLUTE path INSTALL DESTINATION : ${CMAKE_ABSOLUTE_DESTINATION_FILES}")
    endif()
    if(CMAKE_ERROR_ON_ABSOLUTE_INSTALL_DESTINATION)
      message(FATAL_ERROR "ABSOLUTE path INSTALL DESTINATION forbidden (by caller): ${CMAKE_ABSOLUTE_DESTINATION_FILES}")
    endif()
    file(INSTALL DESTINATION "C:/Users/wjdgu/Documents/2023-1-OSSP2-WeAreBility-3/2023-1-OSSP2-WeAreBility-3/frontend/naemansan/build/windows/runner/Debug" TYPE FILE FILES
      "C:/Users/wjdgu/Documents/2023-1-OSSP2-WeAreBility-3/2023-1-OSSP2-WeAreBility-3/frontend/naemansan/build/windows/plugins/geolocator_windows/Debug/geolocator_windows_plugin.dll"
      "C:/Users/wjdgu/Documents/2023-1-OSSP2-WeAreBility-3/2023-1-OSSP2-WeAreBility-3/frontend/naemansan/build/windows/plugins/permission_handler_windows/Debug/permission_handler_windows_plugin.dll"
      )
  elseif(CMAKE_INSTALL_CONFIG_NAME MATCHES "^([Pp][Rr][Oo][Ff][Ii][Ll][Ee])$")
    list(APPEND CMAKE_ABSOLUTE_DESTINATION_FILES
     "C:/Users/wjdgu/Documents/2023-1-OSSP2-WeAreBility-3/2023-1-OSSP2-WeAreBility-3/frontend/naemansan/build/windows/runner/Profile/geolocator_windows_plugin.dll;C:/Users/wjdgu/Documents/2023-1-OSSP2-WeAreBility-3/2023-1-OSSP2-WeAreBility-3/frontend/naemansan/build/windows/runner/Profile/permission_handler_windows_plugin.dll")
    if(CMAKE_WARN_ON_ABSOLUTE_INSTALL_DESTINATION)
      message(WARNING "ABSOLUTE path INSTALL DESTINATION : ${CMAKE_ABSOLUTE_DESTINATION_FILES}")
    endif()
    if(CMAKE_ERROR_ON_ABSOLUTE_INSTALL_DESTINATION)
      message(FATAL_ERROR "ABSOLUTE path INSTALL DESTINATION forbidden (by caller): ${CMAKE_ABSOLUTE_DESTINATION_FILES}")
    endif()
    file(INSTALL DESTINATION "C:/Users/wjdgu/Documents/2023-1-OSSP2-WeAreBility-3/2023-1-OSSP2-WeAreBility-3/frontend/naemansan/build/windows/runner/Profile" TYPE FILE FILES
      "C:/Users/wjdgu/Documents/2023-1-OSSP2-WeAreBility-3/2023-1-OSSP2-WeAreBility-3/frontend/naemansan/build/windows/plugins/geolocator_windows/Profile/geolocator_windows_plugin.dll"
      "C:/Users/wjdgu/Documents/2023-1-OSSP2-WeAreBility-3/2023-1-OSSP2-WeAreBility-3/frontend/naemansan/build/windows/plugins/permission_handler_windows/Profile/permission_handler_windows_plugin.dll"
      )
  elseif(CMAKE_INSTALL_CONFIG_NAME MATCHES "^([Rr][Ee][Ll][Ee][Aa][Ss][Ee])$")
    list(APPEND CMAKE_ABSOLUTE_DESTINATION_FILES
     "C:/Users/wjdgu/Documents/2023-1-OSSP2-WeAreBility-3/2023-1-OSSP2-WeAreBility-3/frontend/naemansan/build/windows/runner/Release/geolocator_windows_plugin.dll;C:/Users/wjdgu/Documents/2023-1-OSSP2-WeAreBility-3/2023-1-OSSP2-WeAreBility-3/frontend/naemansan/build/windows/runner/Release/permission_handler_windows_plugin.dll")
    if(CMAKE_WARN_ON_ABSOLUTE_INSTALL_DESTINATION)
      message(WARNING "ABSOLUTE path INSTALL DESTINATION : ${CMAKE_ABSOLUTE_DESTINATION_FILES}")
    endif()
    if(CMAKE_ERROR_ON_ABSOLUTE_INSTALL_DESTINATION)
      message(FATAL_ERROR "ABSOLUTE path INSTALL DESTINATION forbidden (by caller): ${CMAKE_ABSOLUTE_DESTINATION_FILES}")
    endif()
    file(INSTALL DESTINATION "C:/Users/wjdgu/Documents/2023-1-OSSP2-WeAreBility-3/2023-1-OSSP2-WeAreBility-3/frontend/naemansan/build/windows/runner/Release" TYPE FILE FILES
      "C:/Users/wjdgu/Documents/2023-1-OSSP2-WeAreBility-3/2023-1-OSSP2-WeAreBility-3/frontend/naemansan/build/windows/plugins/geolocator_windows/Release/geolocator_windows_plugin.dll"
      "C:/Users/wjdgu/Documents/2023-1-OSSP2-WeAreBility-3/2023-1-OSSP2-WeAreBility-3/frontend/naemansan/build/windows/plugins/permission_handler_windows/Release/permission_handler_windows_plugin.dll"
      )
  endif()
endif()

if(CMAKE_INSTALL_COMPONENT STREQUAL "Runtime" OR NOT CMAKE_INSTALL_COMPONENT)
  if(CMAKE_INSTALL_CONFIG_NAME MATCHES "^([Dd][Ee][Bb][Uu][Gg])$")
    
  file(REMOVE_RECURSE "C:/Users/wjdgu/Documents/2023-1-OSSP2-WeAreBility-3/2023-1-OSSP2-WeAreBility-3/frontend/naemansan/build/windows/runner/Debug/data/flutter_assets")
  
  elseif(CMAKE_INSTALL_CONFIG_NAME MATCHES "^([Pp][Rr][Oo][Ff][Ii][Ll][Ee])$")
    
  file(REMOVE_RECURSE "C:/Users/wjdgu/Documents/2023-1-OSSP2-WeAreBility-3/2023-1-OSSP2-WeAreBility-3/frontend/naemansan/build/windows/runner/Profile/data/flutter_assets")
  
  elseif(CMAKE_INSTALL_CONFIG_NAME MATCHES "^([Rr][Ee][Ll][Ee][Aa][Ss][Ee])$")
    
  file(REMOVE_RECURSE "C:/Users/wjdgu/Documents/2023-1-OSSP2-WeAreBility-3/2023-1-OSSP2-WeAreBility-3/frontend/naemansan/build/windows/runner/Release/data/flutter_assets")
  
  endif()
endif()

if(CMAKE_INSTALL_COMPONENT STREQUAL "Runtime" OR NOT CMAKE_INSTALL_COMPONENT)
  if(CMAKE_INSTALL_CONFIG_NAME MATCHES "^([Dd][Ee][Bb][Uu][Gg])$")
    list(APPEND CMAKE_ABSOLUTE_DESTINATION_FILES
     "C:/Users/wjdgu/Documents/2023-1-OSSP2-WeAreBility-3/2023-1-OSSP2-WeAreBility-3/frontend/naemansan/build/windows/runner/Debug/data/flutter_assets")
    if(CMAKE_WARN_ON_ABSOLUTE_INSTALL_DESTINATION)
      message(WARNING "ABSOLUTE path INSTALL DESTINATION : ${CMAKE_ABSOLUTE_DESTINATION_FILES}")
    endif()
    if(CMAKE_ERROR_ON_ABSOLUTE_INSTALL_DESTINATION)
      message(FATAL_ERROR "ABSOLUTE path INSTALL DESTINATION forbidden (by caller): ${CMAKE_ABSOLUTE_DESTINATION_FILES}")
    endif()
    file(INSTALL DESTINATION "C:/Users/wjdgu/Documents/2023-1-OSSP2-WeAreBility-3/2023-1-OSSP2-WeAreBility-3/frontend/naemansan/build/windows/runner/Debug/data" TYPE DIRECTORY FILES "C:/Users/wjdgu/Documents/2023-1-OSSP2-WeAreBility-3/2023-1-OSSP2-WeAreBility-3/frontend/naemansan/build//flutter_assets")
  elseif(CMAKE_INSTALL_CONFIG_NAME MATCHES "^([Pp][Rr][Oo][Ff][Ii][Ll][Ee])$")
    list(APPEND CMAKE_ABSOLUTE_DESTINATION_FILES
     "C:/Users/wjdgu/Documents/2023-1-OSSP2-WeAreBility-3/2023-1-OSSP2-WeAreBility-3/frontend/naemansan/build/windows/runner/Profile/data/flutter_assets")
    if(CMAKE_WARN_ON_ABSOLUTE_INSTALL_DESTINATION)
      message(WARNING "ABSOLUTE path INSTALL DESTINATION : ${CMAKE_ABSOLUTE_DESTINATION_FILES}")
    endif()
    if(CMAKE_ERROR_ON_ABSOLUTE_INSTALL_DESTINATION)
      message(FATAL_ERROR "ABSOLUTE path INSTALL DESTINATION forbidden (by caller): ${CMAKE_ABSOLUTE_DESTINATION_FILES}")
    endif()
    file(INSTALL DESTINATION "C:/Users/wjdgu/Documents/2023-1-OSSP2-WeAreBility-3/2023-1-OSSP2-WeAreBility-3/frontend/naemansan/build/windows/runner/Profile/data" TYPE DIRECTORY FILES "C:/Users/wjdgu/Documents/2023-1-OSSP2-WeAreBility-3/2023-1-OSSP2-WeAreBility-3/frontend/naemansan/build//flutter_assets")
  elseif(CMAKE_INSTALL_CONFIG_NAME MATCHES "^([Rr][Ee][Ll][Ee][Aa][Ss][Ee])$")
    list(APPEND CMAKE_ABSOLUTE_DESTINATION_FILES
     "C:/Users/wjdgu/Documents/2023-1-OSSP2-WeAreBility-3/2023-1-OSSP2-WeAreBility-3/frontend/naemansan/build/windows/runner/Release/data/flutter_assets")
    if(CMAKE_WARN_ON_ABSOLUTE_INSTALL_DESTINATION)
      message(WARNING "ABSOLUTE path INSTALL DESTINATION : ${CMAKE_ABSOLUTE_DESTINATION_FILES}")
    endif()
    if(CMAKE_ERROR_ON_ABSOLUTE_INSTALL_DESTINATION)
      message(FATAL_ERROR "ABSOLUTE path INSTALL DESTINATION forbidden (by caller): ${CMAKE_ABSOLUTE_DESTINATION_FILES}")
    endif()
    file(INSTALL DESTINATION "C:/Users/wjdgu/Documents/2023-1-OSSP2-WeAreBility-3/2023-1-OSSP2-WeAreBility-3/frontend/naemansan/build/windows/runner/Release/data" TYPE DIRECTORY FILES "C:/Users/wjdgu/Documents/2023-1-OSSP2-WeAreBility-3/2023-1-OSSP2-WeAreBility-3/frontend/naemansan/build//flutter_assets")
  endif()
endif()

if(CMAKE_INSTALL_COMPONENT STREQUAL "Runtime" OR NOT CMAKE_INSTALL_COMPONENT)
  if(CMAKE_INSTALL_CONFIG_NAME MATCHES "^([Pp][Rr][Oo][Ff][Ii][Ll][Ee])$")
    list(APPEND CMAKE_ABSOLUTE_DESTINATION_FILES
     "C:/Users/wjdgu/Documents/2023-1-OSSP2-WeAreBility-3/2023-1-OSSP2-WeAreBility-3/frontend/naemansan/build/windows/runner/Profile/data/app.so")
    if(CMAKE_WARN_ON_ABSOLUTE_INSTALL_DESTINATION)
      message(WARNING "ABSOLUTE path INSTALL DESTINATION : ${CMAKE_ABSOLUTE_DESTINATION_FILES}")
    endif()
    if(CMAKE_ERROR_ON_ABSOLUTE_INSTALL_DESTINATION)
      message(FATAL_ERROR "ABSOLUTE path INSTALL DESTINATION forbidden (by caller): ${CMAKE_ABSOLUTE_DESTINATION_FILES}")
    endif()
    file(INSTALL DESTINATION "C:/Users/wjdgu/Documents/2023-1-OSSP2-WeAreBility-3/2023-1-OSSP2-WeAreBility-3/frontend/naemansan/build/windows/runner/Profile/data" TYPE FILE FILES "C:/Users/wjdgu/Documents/2023-1-OSSP2-WeAreBility-3/2023-1-OSSP2-WeAreBility-3/frontend/naemansan/build/windows/app.so")
  elseif(CMAKE_INSTALL_CONFIG_NAME MATCHES "^([Rr][Ee][Ll][Ee][Aa][Ss][Ee])$")
    list(APPEND CMAKE_ABSOLUTE_DESTINATION_FILES
     "C:/Users/wjdgu/Documents/2023-1-OSSP2-WeAreBility-3/2023-1-OSSP2-WeAreBility-3/frontend/naemansan/build/windows/runner/Release/data/app.so")
    if(CMAKE_WARN_ON_ABSOLUTE_INSTALL_DESTINATION)
      message(WARNING "ABSOLUTE path INSTALL DESTINATION : ${CMAKE_ABSOLUTE_DESTINATION_FILES}")
    endif()
    if(CMAKE_ERROR_ON_ABSOLUTE_INSTALL_DESTINATION)
      message(FATAL_ERROR "ABSOLUTE path INSTALL DESTINATION forbidden (by caller): ${CMAKE_ABSOLUTE_DESTINATION_FILES}")
    endif()
    file(INSTALL DESTINATION "C:/Users/wjdgu/Documents/2023-1-OSSP2-WeAreBility-3/2023-1-OSSP2-WeAreBility-3/frontend/naemansan/build/windows/runner/Release/data" TYPE FILE FILES "C:/Users/wjdgu/Documents/2023-1-OSSP2-WeAreBility-3/2023-1-OSSP2-WeAreBility-3/frontend/naemansan/build/windows/app.so")
  endif()
endif()

if(CMAKE_INSTALL_COMPONENT)
  set(CMAKE_INSTALL_MANIFEST "install_manifest_${CMAKE_INSTALL_COMPONENT}.txt")
else()
  set(CMAKE_INSTALL_MANIFEST "install_manifest.txt")
endif()

string(REPLACE ";" "\n" CMAKE_INSTALL_MANIFEST_CONTENT
       "${CMAKE_INSTALL_MANIFEST_FILES}")
file(WRITE "C:/Users/wjdgu/Documents/2023-1-OSSP2-WeAreBility-3/2023-1-OSSP2-WeAreBility-3/frontend/naemansan/build/windows/${CMAKE_INSTALL_MANIFEST}"
     "${CMAKE_INSTALL_MANIFEST_CONTENT}")
