# grades-vaadin（Vaadin 25）のイメージを作る（本番モードでビルドする）
# 対応する資料：Docker 編 05 章 7 節
# ビルドは、このフォルダで：  docker build -t grades-vaadin:1.0 .
# DB と一緒に動かすときは、docker/compose.vaadin.yaml を使う。
# 初回のビルドは、画面用の部品（Node.js など）のダウンロードがあり、時間がかかる。

# ---- ステージ 1：ビルド（JDK） ----
FROM eclipse-temurin:25-jdk AS build
WORKDIR /workspace

COPY mvnw pom.xml ./
COPY .mvn .mvn
RUN ./mvnw dependency:go-offline

COPY src src
# -Pproduction：画面用のファイルを最適化し、開発用の部品（vaadin-dev）を含めない
RUN ./mvnw -Pproduction -DskipTests clean package

# ---- ステージ 2：実行（JRE） ----
FROM eclipse-temurin:25-jre
WORKDIR /app

RUN useradd --system --no-create-home app
USER app

COPY --from=build /workspace/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
