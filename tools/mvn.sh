#!/bin/zsh

# 日志输出
log(){
  echo -e "===========================$1=======================\n"
}

# 执行mnv命令
#execMvn(){
#  file=$1
#  groupId=$2
#  version=$3
#  name=${file%.*}
#      log "$name mvn start"
#      # 执行本地mvn脚本命令
#      log "执行脚本命令：groupId：$groupId  version：$version  file：$file  name：$name"
#      mvn deploy:deploy-file -Dfile=$file -Durl="file://." -DgroupId="$groupId" -DartifactId="$name" -Dversion="$version"
#      log "$name mvn end"
#}

# 执行mnv命令
execMvn(){
  file=$1
  groupId=$2
  version=$3
  name=${file%.*}
      log "$name mvn start"
      # 执行本地mvn脚本命令
      log "执行脚本命令：groupId：$groupId  version：$version  file：$file  name：$name"
      mvn deploy:deploy-file -Dfile=$file -Durl="file://." -DgroupId="$groupId" -DartifactId="$name" -Dversion="$version"
      log "$name mvn end"
}

transformGroup(){
  groupPath=$1
  file=$2
  if [[ $groupPath == *"."* ]]; then
      log "$groupPath 替换. -> /"
      groupPath="${groupPath//"."/"/"}"
      if [[ $file == *"."* ]]; then
        log "保留$file 中的.左边的内容"
        groupPath="$groupPath/${file%.*}"
      fi
  fi
}

# 删除之前存在的本地文件
deleteDoc(){
  log "移除原始库"
  rootPath=$1
  deleteDocPath="$rootPath/$2"
  log "准备移除库:$deleteDocPath"
  cd $rootPath
  #存在目录进行删除
  if [ -d "$deleteDocPath" ]; then
    echo "移除libs下的$deleteDocPath 路径"
    rm -r $deleteDocPath
  else
    log "目录不存在"
  fi
}

# 将目录移动到指定位置
moveDoc(){
  log "切换新库"
  cd $1
  log "$1/$2/$3"
  if [ -d "$1/android/libs/$2" ]; then
    log "gorupPath路径存在"
    if [ -d "$1/android/libs/$2/$3" ]; then
        log "存在路径，先删除指定子文件"
        # 存在路径，先删除指定子文件
        rm -r "$1/android/libs/$2/$3"
    fi
    else
        log "gorupPath路径不存在，先创建目录"
#         不存在路径，先创建目录
        mkdir -p "$1/android/libs/$2"
    fi

  # 将mvn库，移动到指定目录下
  mv -f "$1/$2/$3" "$1/android/libs/$2"
}

# 删除根目录下的groupPath
deleteRootGroupPath(){
  groupPath=$2
  if [[ $groupPath == *"."* ]]; then
    groupPath=${groupPath%.*}
  fi
  deleteDoc $1 $groupPath
}

# 定义查找当前目录下的所有aar文件
findAar(){
  # 获取当前脚本所在目录
  filePath="$(pwd)"
  log "当前脚本所在目录：$filePath"
  # 删除根目录下是否存在旧库
  deleteRootGroupPath "$filePath" "$1"
  for file in $(ls $filePath); do
    # 判断当前aar的名字是debug还是release
    if [[ $file == *"debug"* ]] || [[ $file == *"release"* ]]; then
      log "入参：$1 $2"
      execMvn $file $1 $2
      # 检查mvn是否执行成功，查看根目录下是否存有com目录
      # 将groupId转为path
      groupPath=$1
      if [[ $groupPath == *"."* ]]; then
          log "$groupPath 替换. -> /"
          groupPath="${groupPath//"."/"/"}"
      fi
#      `transformGroup $1 $file`
      if [[ $file == *"."* ]]; then
            log "保留$file 中的.左边的内容"
            fileDoc="${file%.*}"
          fi
      log "groupPath $filePath/$groupPath/$fileDoc"
      mvnPath="$filePath/$groupPath"
      log "生成mvn地址:$mvnPath"
      if [ -d "$mvnPath" ]; then
        # 将生成的mvn工程移动至libs下
        moveDoc "$filePath" "$groupPath" "$fileDoc"
      else
        log "生成mvn库的目录地址不存在"
      fi
    fi
  done
  log "aar生成本地mvn库完成"
  # 删除原有文件

  deleteRootGroupPath "$filePath" "$1"

}

confirmLogic(){
# 判断删除路径是否有指定，未指定则删除默认/libs/com路径
  if [ -z $3 ]; then
    findAar $1 $2
  else
    findAar $1 $2
  fi

}


# 脚本运行前，检查是否输入groupId version
echo "----------脚本入参 $@"
if [ "$*" -lt 2 ]; then
    log "请输入groupId、version"
    exit
fi

confirmLogic $1 $2 $3

