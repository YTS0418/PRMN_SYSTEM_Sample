#!/bin/bash
# フォルダごとにコピーを持っている SQL が、ずれていないかを確認する。
# リポジトリのルートで実行する： bash tools/check-db-copies.sh
# 正本は database/db。architecture/db などは、そのコピー（frontend/db は users テーブルを足すため対象外）。
set -u
status=0
for copy in architecture/db; do
  if diff -r database/db "$copy" > /dev/null; then
    echo "OK     : $copy は database/db と同じ"
  else
    echo "DIFFER : $copy は database/db と違います"
    diff -r database/db "$copy"
    status=1
  fi
done
exit $status
