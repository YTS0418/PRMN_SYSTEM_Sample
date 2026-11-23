# database — データベース編のサンプル

[🏠 サンプルの目次](../README.md#目次) ｜ [📖 対応する資料：データベース編](https://github.com/YTS0418/PRMN_SYSTEM_Lecture/blob/main/データベース/00_はじめに.md)

資料：[データベース編](https://github.com/YTS0418/PRMN_SYSTEM_Lecture/blob/main/データベース/00_はじめに.md)。テーブル定義とサンプルデータ（SQL）と、Java から PostgreSQL を使う例（JDBC）です。この README だけで、動かせます。

> [!NOTE]
> コード・層の呼び名・日本語の名前の決まりは、[ルートの README](../README.md#コードの書き方の決まり) にあります。

## 構成

| フォルダ | 内容 | 対応する資料 |
|---|---|---|
| [`db/`](db/) | テーブル定義（`01_schema.sql`）とサンプルデータ（`02_seed.sql`） | データベース編 [06](https://github.com/YTS0418/PRMN_SYSTEM_Lecture/blob/main/データベース/06_物理設計.md)〜[11](https://github.com/YTS0418/PRMN_SYSTEM_Lecture/blob/main/データベース/11_トランザクション.md) 章 |
| [`step2-jdbc/`](step2-jdbc/) | 4 層に分けた成績管理の、保存先を **PostgreSQL**（JdbcClient）にしたもの | データベース編 [12 章](https://github.com/YTS0418/PRMN_SYSTEM_Lecture/blob/main/データベース/12_JavaからDBを使う_JDBC.md) |

## 準備

| 必要なもの | 備考 |
|---|---|
| JDK 25 | `java -version` で確認する |
| PostgreSQL | [データベース編 07 章](https://github.com/YTS0418/PRMN_SYSTEM_Lecture/blob/main/データベース/07_PostgreSQL環境構築.md) |
| Maven | `step2-jdbc` で使う。IDE（IntelliJ IDEA など）で開いてもよい |

## データベースを用意する

PostgreSQL にデータベース `grades_db` を作り、テーブルとサンプルデータを入れます。**このフォルダ**（`database/`）で実行してください。SQL の書き方は、[データベース編 08 章](https://github.com/YTS0418/PRMN_SYSTEM_Lecture/blob/main/データベース/08_SQL_DDL.md)・[09 章](https://github.com/YTS0418/PRMN_SYSTEM_Lecture/blob/main/データベース/09_SQL_基本操作.md)・[10 章](https://github.com/YTS0418/PRMN_SYSTEM_Lecture/blob/main/データベース/10_SQL_応用.md)にあります。

```bash
psql -U postgres -c "CREATE DATABASE grades_db"
psql -U postgres -d grades_db -f db/01_schema.sql
psql -U postgres -d grades_db -f db/02_seed.sql
```

`psql` は、PostgreSQL に接続して SQL を実行するコマンドです。`-` で始まる部分（オプション）で、「誰として」「どのデータベースに」「何を実行するか」を指定します。

| 部分 | 意味 | この例では |
|---|---|---|
| `-U postgres` | 接続する**ユーザー**（User）の名前 | `postgres`（PostgreSQL を入れたときに作られる管理者） |
| `-d grades_db` | 接続する**データベース**（database）の名前 | `grades_db`。省略すると、ユーザー名と同じ名前のデータベースに接続する |
| `-c "SQL 文"` | `"` で囲んだ **SQL 文を 1 つ実行**して終了する（command） | `CREATE DATABASE grades_db`（データベースを作る） |
| `-f ファイル` | **ファイルに書かれた SQL** を上から順に実行して終了する（file） | `db/01_schema.sql`（テーブルを作る）、`db/02_seed.sql`（サンプルデータを入れる） |

- 1 行目に `-d` がないのは、`grades_db` を**これから作る**ので、まだ接続できないためです。
- `-f` のパスは、今いるフォルダから見た場所です。そのため、このフォルダ（`database/`）で実行します。
- 実行すると、ユーザー `postgres` の**パスワード**を聞かれます（PostgreSQL を入れたときに決めたもの）。入力しても画面には表示されません。
- `-c` も `-f` も付けずに `psql -U postgres -d grades_db` とすると、SQL を 1 行ずつ打ち込める画面になります（`\q` で終了）。

> [!WARNING]
> **Windows で `enrollments` や外部キー（`foreign key`）のエラーが出るとき**：`psql` の文字コードが UTF-8 ではないのが原因です。`.sql` ファイルの日本語を読み違えて、一部の `INSERT` が失敗し、そのあとの `INSERT`（`courses` や `enrollments`）も、つながりを失ってエラーになります。先に環境変数 `PGCLIENTENCODING` を `UTF8` にしてから、`01_schema.sql` → `02_seed.sql` の順に流し直してください（コマンドプロンプトは `set PGCLIENTENCODING=UTF8`、PowerShell は `$env:PGCLIENTENCODING = 'UTF8'`）。

> [!WARNING]
> `01_schema.sql` は、学習用に**テーブルを先に全部消してから作り直します**。すでに大事なデータが入っている `grades_db` には流さないでください。やり直したいときも、`01_schema.sql` → `02_seed.sql` の順に流し直せば、最初の状態に戻ります。

### パスワードの渡し方

このフォルダのアプリは、データベースのパスワードを**環境変数 `DB_PASSWORD`** から読みます。パスワードをコードや設定ファイルに書かないためです。起動するターミナルで、先に設定してください（そのターミナルを閉じるまで有効です）。

| 環境 | 設定の仕方 |
|---|---|
| macOS / Linux / Git Bash | `export DB_PASSWORD='あなたのパスワード'` |
| Windows コマンドプロンプト | `set DB_PASSWORD=あなたのパスワード` |
| Windows PowerShell | `$env:DB_PASSWORD = 'あなたのパスワード'` |

> パスワードを `.env` などのファイルに書く場合は、Git に入れないでください（`.gitignore` に `.env` を入れてあります）。

設定していないと、「環境変数 `DB_PASSWORD` が設定されていません」と表示して終了します。

## step2-jdbc：PostgreSQL 版を動かす

JdbcClient の使い方と、Repository の実装は、[データベース編 12 章](https://github.com/YTS0418/PRMN_SYSTEM_Lecture/blob/main/データベース/12_JavaからDBを使う_JDBC.md)にあります。

```bash
cd step2-jdbc
mvn compile exec:java
```

| 部分 | 意味 |
|---|---|
| `mvn` | Maven（Java のビルドツール）のコマンド。`pom.xml` に書かれたライブラリを自動でダウンロードしてくれる |
| `compile` | `src` のコードをコンパイルする |
| `exec:java` | コンパイルしたプログラムを起動する（どのクラスを起動するかは、`pom.xml` の `mainClass` に書いてある） |

`compile exec:java` のように並べると、左から順に実行されます。初回は、ライブラリのダウンロードに少し時間がかかります。

操作は step1 と同じです。違いは、**アプリを終了してもデータが残る**ことです（もう一度起動して、成績一覧を表示してみてください）。

接続先は、既定で `jdbc:postgresql://localhost:5432/grades_db`、ユーザーは `postgres` です。変える場合だけ、環境変数 `DB_URL` と `DB_USER` を設定します。

## 保存先を差し替えても、他の層は変わらない

`step2-jdbc` の `domain`・`usecase`・`presentation` は、メモリ版（4層アーキテクチャ編の `step1-memory`）と、**同じ内容**です。保存先を差し替えたのは、`dataaccess` と `Main` だけです（なぜ他の層が変わらないのかは、[4層編 03 章](https://github.com/YTS0418/PRMN_SYSTEM_Lecture/blob/main/4層アーキテクチャ/03_依存の向きとインタフェース.md)で説明しています）。このリポジトリの `architecture/` と比べて、確かめられます（リポジトリのルートで実行）。

```bash
diff -r architecture/step1-memory/src/grades/domain       database/step2-jdbc/src/grades/domain
diff -r architecture/step1-memory/src/grades/usecase      database/step2-jdbc/src/grades/usecase
diff -r architecture/step1-memory/src/grades/presentation database/step2-jdbc/src/grades/presentation
```

| 部分 | 意味 |
|---|---|
| `diff` | 2 つのファイル（フォルダ）を比べて、違う行を表示するコマンド |
| `-r` | フォルダの中身を、下のフォルダまですべて比べる（recursive） |
| 1 つ目・2 つ目のパス | 比べる 2 つのフォルダ |

何も表示されなければ、同じ内容です。

---

[🏠 サンプルの目次](../README.md#目次) ｜ [📖 対応する資料：データベース編](https://github.com/YTS0418/PRMN_SYSTEM_Lecture/blob/main/データベース/00_はじめに.md)
