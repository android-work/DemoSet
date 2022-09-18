#!/bin/bash

cd ..
# 回退到根目录
root=`pwd`
echo "请输入需要dx的文件"
read name
echo "输入的是文件名:$name"

# 进入目标查询目录
cd $root/app/src/main/java
artPath=`pwd`

# 循环查询目标文件
checkArtFile(){
  echo "checkArtFile: $1"
  for dir_name in `ls $1`
 do
   echo "for: $dir_name"
    if [ -d $dir_name ]; then
        echo $1/$dir_name
        checkArtFile $1/$dir_name
    #elif [ -f $dir_name ]; then
       else
         echo "else $1/$dir_name"
    fi
  done
}

checkArtFile $artPath



