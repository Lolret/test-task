echo "setup.sh starts"
whoami
FILE=data.json
if test -f "$FILE"; then
    echo "prepear $FILE for db"
    cat data.json | jq -cr '.[]' | sed 's/\\[tn]//g' >  /var/lib/postgresql/output.json
fi

echo "setup.sh ends"
