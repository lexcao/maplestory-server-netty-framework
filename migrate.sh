#!/bin/zsh

scope="./src/main/resources/db/migration"

function info {
    echo -e "\033[42;30m INFO \033[40;32m\ue0b0\033[0m" $@
}

function warn {
    echo -e "\033[43;30m WARN \033[40;33m\ue0b0\033[0m" $@
}

function error {
    echo -e "\033[41;37m ERROR \033[40;31m\ue0b0\033[0m" $@
}

if [ $# -ne 1 ]; then
    error "Must have migration name!"
    exit -1
fi

file_name=V$(date "+%Y%m%d%H%M%S")_${RANDOM}__$1.sql

info "Creating a migration with name:" ${file_name}

for i in `ls ${scope} | egrep "^.*\.sql$"`; do
    if [ ${file_name} = ${i} ]; then
        error "Create migration" ${file_name} "failed! It is exist!"
        exit -2
    fi
done

touch ${scope}/${file_name}
info ${scope}/${file_name} "created."
