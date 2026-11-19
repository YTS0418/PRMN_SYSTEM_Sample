# step3-spring（Spring Boot の Web API）のイメージを作る
# 対応する資料：Docker 編 05 章
# ビルドは、このフォルダで：  docker build -t grades-app:1.0 .
# DB と一緒に動かすときは、docker/compose.app.yaml を使う。

# ---- ステージ 1：ビルド（JDK） ----
FROM eclipse-temurin:25-jdk AS build
WORKDIR /workspace

# 変わりにくいものを先に（キャッシュを効かせる）
COPY mvnw pom.xml ./
COPY .mvn .mvn
RUN ./mvnw dependency:go-offline

# 変わりやすいものを後に
COPY src src
RUN ./mvnw -DskipTests package

# ---- ステージ 2：実行（JRE） ----
FROM eclipse-temurin:25-jre
WORKDIR /app

# root 以外のユーザーで動かす
RUN useradd --system --no-create-home app
USER app

COPY --from=build /workspace/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
