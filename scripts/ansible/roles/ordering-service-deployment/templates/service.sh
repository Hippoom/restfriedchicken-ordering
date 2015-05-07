#!/bin/sh -ex
PWD="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

ordering_dir={{ ordering_dir }}
ordering_binary=current

APP_NAME="ordering"

APP_USAGE="Usage: $0 {\e[00;32mstart\e[00m|\e[00;31mstop\e[00m|\e[00;32mstatus\e[00m|\e[00;31mrestart\e[00m}"

#SHUTDOWN_WAIT is wait time in seconds for java proccess to stop
SHUTDOWN_WAIT=20

app_pid() {
    echo $(ps -ef | grep "$ordering_dir/$ordering_binary" | grep -v grep | awk '{print $2}')
}

start() {
  pid=$(app_pid)
  if [ -n "$pid" ]
  then
    echo -e "\e[00;31m$APP_NAME is already running (pid: $pid)\e[00m"
  else
    echo -e "\e[00;32mStarting $APP_NAME\e[00m"
    nohup java -jar $ordering_dir/$ordering_binary >/dev/null 2>&1 &
    status
  fi
  return 0
}

status() {
  pid=$(app_pid)
  if [ -n "$pid" ]; then echo -e "\e[00;32m$APP_NAME is running with pid: $pid\e[00m"
  else echo -e "\e[00;31m$APP_NAME is not running\e[00m"
  fi
}

stop() {
  pid=$(app_pid)
  if [ -n "$pid" ]
  then
    echo -e "\e[00;31mStoping $APP_NAME\e[00m"

    kill $pid

    let kwait=$SHUTDOWN_WAIT
    count=0;
    until [ `ps -p $pid | grep -c $pid` = '0' ] || [ $count -gt $kwait ]
    do
      echo -n -e "\n\e[00;31mwaiting for processes to exit\e[00m";
      sleep 1
      let count=$count+1;
    done

    if [ $count -gt $kwait ]; then
      echo -n -e "\n\e[00;31mkilling processes which didn't stop after $SHUTDOWN_WAIT seconds\e[00m"
      kill -9 $pid
    fi
  else
    echo -e "\e[00;31m$APP_NAME is not running\e[00m"
  fi

  return 0
}

case $1 in

    start)
      start
    ;;

    stop)
      stop
    ;;

    restart)
      stop
      start
    ;;

    status)
        status

    ;;

    *)
        echo -e $APP_USAGE
    ;;
esac
exit 0