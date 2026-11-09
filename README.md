# PRMN_SYSTEM_Sample

公立千歳科学技術大学のプロジェクトメンバー（システム班）向け教育資料 [PRMN_SYSTEM_Lecture](https://github.com/YTS0418/PRMN_SYSTEM_Lecture) の、**サンプルリポジトリ**です。資料の「データベース編」と「4層アーキテクチャ編」に出てくる、動く完成コードをまとめています。

題材は、資料と同じ**成績管理システム**です（学生の登録、成績の登録、成績一覧の表示）。

> 資料では考え方（なぜ・何を・どこに）を説明し、このリポジトリでは**どう書くか**を確認します。資料を読みながら、このリポジトリのコードを IDE で開いておくのがおすすめです。

## 構成

| フォルダ | 内容 | 対応する資料 |
|---|---|---|
| [`db/`](db/) | テーブル定義とサンプルデータ（SQL） | データベース編 06〜11 章 |
| [`step1-memory/`](step1-memory/) | 4 層に分けた成績管理。保存先は**メモリ**（コンソールで操作） | 4層編 04・06 章 |
| [`step2-jdbc/`](step2-jdbc/) | step1 の保存先を **PostgreSQL**（JdbcClient）に差し替えたもの | データベース編 12 章 |
| [`step3-spring/`](step3-spring/) | **Spring Boot** で書き直したもの（Web API） | 4層編 07 章 |

step1 → step2 → step3 の順に、同じ成績管理システムが変わっていきます。

| 層 | step1 | step2 | step3 |
|---|---|---|---|
| Presentation | `ConsoleApp` | **同じ** | `StudentController`（HTTP） |
| Application | `GradeService` | **同じ** | `GradeService`（アノテーションを付けただけ） |
| Domain | `Student`、`Enrollment` など | **同じ** | **同じ** |
| Infrastructure | `InMemory…Repository` | `Jdbc…Repository` を追加 | `Jdbc…Repository`（`@Repository`） |
| 組み立て | `Main` | `Main`（`new` の差し替えだけ） | Spring（DI コンテナ） |

「保存先を差し替えても、Domain・Application・Presentation は 1 行も変わらない」ことが、step1 と step2 を比べると確認できます。

```bash
diff -r step1-memory/src/grades/domain       step2-jdbc/src/grades/domain
diff -r step1-memory/src/grades/application  step2-jdbc/src/grades/application
diff -r step1-memory/src/grades/presentation step2-jdbc/src/grades/presentation
```

何も表示されなければ、同じ内容です。

## 準備

| 必要なもの | 使う step | 備考 |
|---|---|---|
| JDK 25 | すべて | `java -version` で確認する（[Java 入門 01 章](https://github.com/YTS0418/PRMN_SYSTEM_Lecture/blob/main/付録/Java入門/01_Javaとは・環境構築.md)） |
| PostgreSQL | step2、step3 | [データベース編 07 章](https://github.com/YTS0418/PRMN_SYSTEM_Lecture/blob/main/データベース/07_PostgreSQL環境構築.md) |
| Maven | step2 | step3 は同梱の `mvnw` を使うので不要。IDE（IntelliJ IDEA など）で開いてもよい |

step1 は、PostgreSQL も Maven も要りません。まずは step1 から試してください。

## step1：メモリ版を動かす

```bash
cd step1-memory
java src/grades/Main.java
```

メニューから、学生の登録 → 成績の登録 → 成績一覧の表示を試せます。**アプリを終了すると、データは消えます**（保存先がメモリのため）。

点数に `150` を入れると、`エラー: 点数は0〜100にしてください: 150` と表示されます。このエラーは Domain 層（`Enrollment`）が出したものです。

## データベースを用意する（step2・step3 の前に）

PostgreSQL にデータベース `grades_db` を作り、テーブルとサンプルデータを入れます。**リポジトリのルート**で実行してください。

```bash
psql -U postgres -c "CREATE DATABASE grades_db"
psql -U postgres -d grades_db -f db/01_schema.sql
psql -U postgres -d grades_db -f db/02_seed.sql
```

> [!WARNING]
> `01_schema.sql` は、学習用に**テーブルを先に全部消してから作り直します**。すでに大事なデータが入っている `grades_db` には流さないでください。やり直したいときも、`01_schema.sql` → `02_seed.sql` の順に流し直せば、最初の状態に戻ります。

### パスワードの渡し方

step2 と step3 は、データベースのパスワードを**環境変数 `DB_PASSWORD`** から読みます。パスワードをコードや設定ファイルに書かないためです。起動するターミナルで、先に設定してください（そのターミナルを閉じるまで有効です）。

| 環境 | 設定の仕方 |
|---|---|
| macOS / Linux / Git Bash | `export DB_PASSWORD='あなたのパスワード'` |
| Windows コマンドプロンプト | `set DB_PASSWORD=あなたのパスワード` |
| Windows PowerShell | `$env:DB_PASSWORD = 'あなたのパスワード'` |

> パスワードを `.env` などのファイルに書く場合は、Git に入れないでください（`.gitignore` に `.env` を入れてあります）。

## step2：PostgreSQL 版を動かす

```bash
cd step2-jdbc
mvn compile exec:java
```

操作は step1 と同じです。違いは、**アプリを終了してもデータが残る**ことです（もう一度起動して、成績一覧を表示してみてください）。

接続先は、既定で `jdbc:postgresql://localhost:5432/grades_db`、ユーザーは `postgres` です。変える場合だけ、環境変数 `DB_URL` と `DB_USER` を設定します。

## step3：Spring Boot 版を動かす

```bash
cd step3-spring
./mvnw spring-boot:run          # Windows のコマンドプロンプトでは  mvnw.cmd spring-boot:run
```

ログに `Started GradesApplication` と出れば起動成功です。ポート 8080 で待ち受けます（止めるときは `Ctrl+C`）。接続先は `src/main/resources/application.properties` にあります。

### API

| 機能 | HTTP | URL | 成功時 |
|---|---|---|---|
| 学生を登録する | `POST` | `/students` | `200`（登録した学生の JSON） |
| 成績を登録する | `PUT` | `/students/{id}/enrollments` | `204`（本文なし） |
| 成績一覧を表示する | `GET` | `/students/{id}/transcript` | `200`（成績一覧の JSON） |

| 状況 | ステータス |
|---|---|
| 学生や科目が存在しない | `404` |
| 入力が業務のルールに合わない（点数が範囲外など） | `400` |
| データベースの制約に反する（存在しない学科の指定など） | `409` |

### 試す（GET）

`GET` はブラウザでも確認できます。

```bash
curl http://localhost:8080/students/1/transcript
```

### 試す（POST / PUT）

JSON のクォートの書き方が、環境によって違います。**自分の環境の書き方**を使ってください。

**macOS / Linux / Git Bash**

```bash
curl -X POST -H "Content-Type: application/json" \
     -d '{"name":"Mika","departmentId":3,"enrolledYear":2025}' \
     http://localhost:8080/students

curl -X PUT -H "Content-Type: application/json" \
     -d '{"courseId":1,"academicYear":2025,"score":80}' \
     http://localhost:8080/students/1/enrollments
```

**Windows コマンドプロンプト**（`"` を `\"` にする。1 行で書く）

```bat
curl -X POST -H "Content-Type: application/json" -d "{\"name\":\"Mika\",\"departmentId\":3,\"enrolledYear\":2025}" http://localhost:8080/students

curl -X PUT -H "Content-Type: application/json" -d "{\"courseId\":1,\"academicYear\":2025,\"score\":80}" http://localhost:8080/students/1/enrollments
```

**Windows PowerShell**（`curl` は別のコマンドの別名なので、`Invoke-RestMethod` を使う）

```powershell
Invoke-RestMethod -Method Post -Uri http://localhost:8080/students `
  -ContentType 'application/json' -Body '{"name":"Mika","departmentId":3,"enrolledYear":2025}'

Invoke-RestMethod -Method Put -Uri http://localhost:8080/students/1/enrollments `
  -ContentType 'application/json' -Body '{"courseId":1,"academicYear":2025,"score":80}'
```

> 成績の登録で `score` を省く（書かない）と、「履修中」になります。同じ（学生, 科目, 年度）をもう一度登録すると、点数が**上書き**されます。

### 確かめてみよう

- 存在しない学生（`/students/999/transcript`）で `404` が返る
- 範囲外の点数（`150`）で `400` が返り、「点数は0〜100にしてください」と出る（**Domain 層が出したエラー**）
- 同じ成績をもう一度登録しても、データベースの行は増えず、点数が上書きされる

> [!WARNING]
> このサンプルには**認証・認可がありません**。`/students/1/transcript` の `1` を `2` に変えるだけで、誰でも他人の成績を見られます。学習用のサンプルなので省いてあります。本番のアプリケーションでは、必ず認証・認可を加えてください（[4層編 07 章 8 節](https://github.com/YTS0418/PRMN_SYSTEM_Lecture/blob/main/4層アーキテクチャ/07_Spring_Bootで書き直す.md)）。

## 層の約束を確認する

4 層の「依存の向き」は、`import` 文を見れば確認できます（[4層編 06 章 6 節](https://github.com/YTS0418/PRMN_SYSTEM_Lecture/blob/main/4層アーキテクチャ/06_サンプル実装の読み方.md)）。

```bash
# infrastructure を import しているのは、Main だけのはず
grep -rl "import grades.infrastructure" step1-memory/src step2-jdbc/src

# domain が import しているのは、標準ライブラリ（java.util）だけのはず
grep -h "^import" step1-memory/src/grades/domain/*.java | sort -u
```

## 注意

教育用のサンプルです。データはすべて架空のもので、実在の人物・団体とは関係ありません。
