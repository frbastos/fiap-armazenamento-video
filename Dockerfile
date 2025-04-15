# Estágio 1: Construir o JAR
FROM eclipse-temurin:17-jdk-jammy AS build

# Definir o diretório de trabalho dentro do contêiner
WORKDIR /app

# Copiar todos os arquivos do projeto para o contêiner
COPY . .

# Adicione permissão de execução ao script mvnw
RUN chmod +x mvnw

# Executar o build do Maven sem rodar os testes
RUN ./mvnw clean package -DskipTests

# Estágio 2: Construir a imagem final
FROM eclipse-temurin:17-jdk-jammy

# Definir o diretório de trabalho dentro do contêiner
WORKDIR /app

# Copiar o JAR construído no estágio anterior para o diretório de trabalho
COPY --from=build /app/target/*.jar app.jar

# Definir o comando para executar o aplicativo
ENTRYPOINT ["java","-jar","/app/app.jar", "--spring.profiles.active=${SPRING_PROFILES_ACTIVE}"]

# Expor a porta que a aplicação vai usar
EXPOSE 8081