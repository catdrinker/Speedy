#!/bin/bash

# 使用方式
# 例如 :sh pngpress.sh  /Users/panhao/AndroidStudioProjects/ichat_app/folder

read_dir(){
for file in `ls -a $1`
do
  if [ -d $1"/"$file ]
    then
    if [[ $file != '.' && $file != '..' ]]
      then
    read_dir $1"/"$file
    fi
    else
    echo "input is " $1"/"$file
    # 判断如果是png，就进行压缩

    if [ "${file##*.}"x = "png"x ];then
     ../tool/pngquant --quality=80-90 --skip-if-larger --ext=.png --force  $1"/"$file
    fi

  fi
done
}


read_dir $1