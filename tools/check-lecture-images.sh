#!/bin/bash
# docs/images/ の、Lecture からコピーした画像が、元の画像とずれていないかを確認する。
# リポジトリのルートで実行する： bash tools/check-lecture-images.sh
# Lecture のリポジトリは、このリポジトリと同じ親フォルダにある前提（別の場所なら、環境変数 LECTURE_DIR で指定する）。
# コピーの一覧は docs/images/COPIES.txt。
set -u
LECTURE="${LECTURE_DIR:-../PRMN_SYSTEM_Lecture}"
if [ ! -d "$LECTURE/images" ]; then
  echo "Lecture のリポジトリが見つかりません: $LECTURE（環境変数 LECTURE_DIR で指定してください）" >&2
  exit 2
fi
status=0
while IFS=$'\t' read -r copy orig; do
  case "$copy" in ''|'#'*) continue;; esac
  if [ ! -f "$copy" ]; then echo "MISSING: $copy がありません"; status=1; continue; fi
  if cmp -s "$copy" "$LECTURE/images/$orig"; then
    echo "OK     : $copy"
  else
    echo "DIFFER : $copy は $LECTURE/images/$orig と違います"; status=1
  fi
done < docs/images/COPIES.txt
exit $status
