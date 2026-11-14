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
| [`docs/`](docs/) | ユースケース記述と、コードから起こした確認用の文章 | プロメンのガイドライン |

step1 → step2 → step3 の順に、同じ成績管理システムが変わっていきます。

| 層 | パッケージ | step1 | step2 | step3 |
|---|---|---|---|---|
| プレゼンテーション層 | `presentation` | `コンソール画面` | **同じ** | `学生コントローラ`（HTTP） |
| ビジネスロジック層 | `usecase` | `学生を登録する` など 5 クラス | **同じ** | 同じクラス（アノテーションを付けただけ） |
| ドメイン層 | `domain` | `学生`、`履修`、`履修一覧` など | **同じ** | **同じ** |
| データアクセス層 | `dataaccess` | `メモリ…リポジトリ` | `Jdbc…リポジトリ` を追加 | `Jdbc…リポジトリ`（`@Repository`） |
| 組み立て | — | `Main` | `Main`（`new` の差し替えだけ） | Spring（DI コンテナ） |

「保存先を差し替えても、domain・usecase・presentation は 1 行も変わらない」ことが、step1 と step2 を比べると確認できます。

```bash
diff -r step1-memory/src/grades/domain       step2-jdbc/src/grades/domain
diff -r step1-memory/src/grades/usecase      step2-jdbc/src/grades/usecase
diff -r step1-memory/src/grades/presentation step2-jdbc/src/grades/presentation
```

| 部分 | 意味 |
|---|---|
| `diff` | 2 つのファイル（フォルダ）を比べて、違う行を表示するコマンド |
| `-r` | フォルダの中身を、下のフォルダまですべて比べる（recursive） |
| 1 つ目・2 つ目のパス | 比べる 2 つのフォルダ |

何も表示されなければ、同じ内容です。

## コードの書き方の決まり

このサンプルは、プロメンのガイドライン（ユースケース記述 → モデリング → コード）に沿って書いています。

### 層と資料の呼び名

資料（4層アーキテクチャ編）とは、層の呼び名とパッケージ名が次のように対応します。

| このサンプル（ガイドライン） | パッケージ | 資料での呼び名 | 役割 |
|---|---|---|---|
| プレゼンテーション層 | `grades.presentation` | Presentation 層 | 入力を受け取り、結果を表示する |
| ビジネスロジック層 | `grades.usecase` | Application 層 | ユースケースの手順を、上から順に並べる |
| ドメイン層 | `grades.domain` | Domain 層 | 業務のルール（点数の範囲、合格点、平均点の求め方）と、保存の窓口（インタフェース） |
| データアクセス層 | `grades.dataaccess` | Infrastructure 層 | 保存の窓口を、メモリや PostgreSQL で実装する |

### ユースケース 1 つ = クラス 1 つ

`usecase` パッケージには、ユースケースの名前をそのままクラス名にしたクラスを置きます。どのクラスも、メソッドは `実行する` の 1 つだけです。

| ユースケース（クラス名） | `実行する` がすること |
|---|---|
| `学生を登録する` | 学生を作り、学生リポジトリで保存する |
| `成績を登録する` | 学生と科目がいることを確かめ、履修を作り、履修リポジトリで保存する |
| `成績一覧を取得する` | 学生と履修一覧を探し、成績明細と平均点をまとめて成績一覧を作る |
| `学生一覧を取得する` | 画面で学生を選ぶための一覧を返す |
| `科目一覧を取得する` | 画面で科目を選ぶための一覧を返す |

ユースケース記述と、コードから起こした確認用の文章は、[`docs/ユースケース記述.md`](docs/ユースケース記述.md) にあります。

### 日本語プログラミング

クラス名・メソッド名・変数名は、**日本語**で書いています。ユースケース記述に出てくる言葉（学生、履修、点数、保存する など）を、そのままコードの名前にするためです。

| 日本語にするもの | 英語のままにするもの |
|---|---|
| クラス名（`学生`、`成績を登録する`） | パッケージ名（`domain`、`usecase` など。ガイドラインの決まり） |
| メソッド名（`実行する`、`平均点を計算する`） | Java が決めている名前（`Main`、`main`、`String`、`List` など） |
| 変数名・record の項目名（`氏名`、`点数`） | データベースのテーブル名・列名（`students.name` など） |
| | Web API の URL と JSON のキー（`/students`、`"name"` など） |

- 「学生リポジトリ 学生リポジトリ」のように、**型の名前と変数の名前が同じ**になる箇所があります。左が型、右が変数です（英語で `StudentRepository studentRepository` と書くのと同じです）。
- 日本語の名前と、データベースの列名・JSON のキーとの対応は、層の端のクラス（`Jdbc…リポジトリ`、`学生コントローラ`）だけが受け持ちます。

> [!NOTE]
> ファイル名も日本語になります（`学生.java` など）。文字コードは **UTF-8** です。Windows で文字化けやコンパイルエラーが出るときは、エディタと IDE の文字コードが UTF-8 になっているかを確認してください。

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

| 部分 | 意味 |
|---|---|
| `cd step1-memory` | 作業するフォルダを `step1-memory` に移る（change directory） |
| `java` | Java のプログラムを実行するコマンド |
| `src/grades/Main.java` | 起動するファイル。`.java` を直接渡すと、コンパイルしてからそのまま実行する。`Main` が使っているほかのファイルも、`src` の下から自動で探してくれる |

メニューから、学生の登録 → 成績の登録 → 成績一覧の表示を試せます。**アプリを終了すると、データは消えます**（保存先がメモリのため）。

点数に `150` を入れると、`エラー: 点数は0〜100にしてください: 150` と表示されます。このエラーはドメイン層（`履修`）が出したものです。

## データベースを用意する（step2・step3 の前に）

PostgreSQL にデータベース `grades_db` を作り、テーブルとサンプルデータを入れます。**リポジトリのルート**で実行してください。

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
- `-f` のパスは、今いるフォルダから見た場所です。そのため、リポジトリのルートで実行します。
- 実行すると、ユーザー `postgres` の**パスワード**を聞かれます（PostgreSQL を入れたときに決めたもの）。入力しても画面には表示されません。
- `-c` も `-f` も付けずに `psql -U postgres -d grades_db` とすると、SQL を 1 行ずつ打ち込める画面になります（`\q` で終了）。

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

| 部分 | 意味 |
|---|---|
| `mvn` | Maven（Java のビルドツール）のコマンド。`pom.xml` に書かれたライブラリを自動でダウンロードしてくれる |
| `compile` | `src` のコードをコンパイルする |
| `exec:java` | コンパイルしたプログラムを起動する（どのクラスを起動するかは、`pom.xml` の `mainClass` に書いてある） |

`compile exec:java` のように並べると、左から順に実行されます。初回は、ライブラリのダウンロードに少し時間がかかります。

操作は step1 と同じです。違いは、**アプリを終了してもデータが残る**ことです（もう一度起動して、成績一覧を表示してみてください）。

接続先は、既定で `jdbc:postgresql://localhost:5432/grades_db`、ユーザーは `postgres` です。変える場合だけ、環境変数 `DB_URL` と `DB_USER` を設定します。

## step3：Spring Boot 版を動かす

```bash
cd step3-spring
./mvnw spring-boot:run          # Windows のコマンドプロンプトでは  mvnw.cmd spring-boot:run
```

| 部分 | 意味 |
|---|---|
| `./mvnw` | このフォルダに同梱している Maven（Maven Wrapper）。Maven を入れていなくても使える。`./` は「今いるフォルダの」という意味 |
| `spring-boot:run` | Spring Boot のアプリケーションをコンパイルして起動する |
| `#` より右 | コメント（実行されない） |

ログに `Started 成績管理アプリ` と出れば起動成功です。ポート 8080 で待ち受けます（止めるときは `Ctrl+C`）。接続先は `src/main/resources/application.properties` にあります。

### API

| 機能 | HTTP | URL | 成功時 |
|---|---|---|---|
| 学生を登録する | `POST` | `/students` | `200`（登録した学生の JSON） |
| 成績を登録する | `PUT` | `/students/{id}/enrollments` | `204`（本文なし） |
| 成績一覧を取得する | `GET` | `/students/{id}/transcript` | `200`（成績一覧の JSON） |

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

`curl` は、URL にリクエストを送って、返ってきた内容を表示するコマンドです。オプションを付けなければ `GET` で送ります。

### 試す（POST / PUT）

JSON のクォートの書き方が、環境によって違います。**自分の環境の書き方**を使ってください。

| 部分 | 意味 |
|---|---|
| `-X POST` | HTTP メソッドを指定する（`POST`、`PUT` など）。省略すると `GET` |
| `-H "Content-Type: application/json"` | ヘッダー（Header）を付ける。ここでは「本文は JSON です」と伝えている |
| `-d '...'` | 送る本文（data）。ここに JSON を書く |
| 最後の URL | 送り先 |
| 行末の `\` | コマンドが次の行に続く、という印（macOS / Linux / Git Bash） |

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
- 範囲外の点数（`150`）で `400` が返り、「点数は0〜100にしてください」と出る（**ドメイン層が出したエラー**）
- 同じ成績をもう一度登録しても、データベースの行は増えず、点数が上書きされる

> [!WARNING]
> このサンプルには**認証・認可がありません**。`/students/1/transcript` の `1` を `2` に変えるだけで、誰でも他人の成績を見られます。学習用のサンプルなので省いてあります。本番のアプリケーションでは、必ず認証・認可を加えてください（[4層編 07 章 8 節](https://github.com/YTS0418/PRMN_SYSTEM_Lecture/blob/main/4層アーキテクチャ/07_Spring_Bootで書き直す.md)）。

## 層の約束を確認する

4 層の「依存の向き」は、`import` 文を見れば確認できます（[4層編 06 章 6 節](https://github.com/YTS0418/PRMN_SYSTEM_Lecture/blob/main/4層アーキテクチャ/06_サンプル実装の読み方.md)）。

```bash
# dataaccess を import しているのは、Main だけのはず
grep -rl "import grades.dataaccess" step1-memory/src step2-jdbc/src

# domain が import しているのは、標準ライブラリ（java.util）だけのはず
grep -h "^import" step1-memory/src/grades/domain/*.java | sort -u
```

| 部分 | 意味 |
|---|---|
| `grep "文字列" 場所` | ファイルの中から、その文字列を含む行を探すコマンド |
| `-r` | フォルダの中を、下のフォルダまですべて探す（recursive） |
| `-l` | 見つかった行ではなく、**ファイル名だけ**を表示する。`-rl` は `-r` と `-l` をまとめて書いたもの |
| `-h` | ファイル名を付けずに、見つかった行だけを表示する |
| `"^import"` | `^` は「行の先頭」。`import` で始まる行を探す |
| `*.java` | そのフォルダの、名前が `.java` で終わるファイルすべて |
| `\| sort -u` | 左のコマンドの結果を並べ替えて、同じ行を 1 つにまとめる（`\|` は、結果を次のコマンドに渡す記号） |

## 注意

教育用のサンプルです。データはすべて架空のもので、実在の人物・団体とは関係ありません。
