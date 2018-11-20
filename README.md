# API Webedia Backend Challenge

Este projeto é um desafio de programação Back-end dado pela empresa Webedia.

Usando um servidor local e um banco de dados portátil, o programa consiste em uma API REST que simula um simples sistema de blog. Executa todas as operações CRUD para objetos do tipo artigo, autores e comentários, utilizando os principais métodos HTTP (GET, POST, PUT, DELETE).

# Documentação

Segue neste documento README.md, o manual de requisições para utilizar esta API.

Para a documentação do código, foi utilizado a funcionalidade do javadoc. Os arquivos referentes a documentação do javadoc se encontram na pasta /doc do projeto.

# Tecnologias
#### SQLite (DB)
O SQLite é um banco de dados muito utilizado para projetos mobile, pois não necessita de servidor, e todas as informações do banco ficam em um único arquivo.

Por motivos de portabilidade e facilidade de instalação, o SQLite foi escolhido para o projeto.
#### Bibliotecas
O projeto conta com diversas dependências para seu funcionamento, e também para sua produção. As bibliotecas mais relevantes e suas funções são:

* Maven - Gerenciamento de dependências e build.
* Grizzly NIO - Framework para criação de servidor.
* Jersey - Framework para criação de web services REST.
* Gson - Biblioteca para serialização/desserialização de arquivos JSON.
* JUnit 4 - Biblioteca de testes.

# Instalação
O projeto não requer nenhuma instalação complexa, basta ter o Java 8 instalado na máquina.

Um zip na raiz do projeto contém todos os arquivos para o funcionamento do programa, e todos devem ser extraídos no mesmo diretório:
* Arquivo JAR executável.
* Pasta /libs que contém todas as dependências do projeto.
* Pasta /banco-de-dados que contém o banco de dados já configurado e com dados gravados.

Esses arquivos podem ser encontrados também dentro da pasta /target.

Com tudo devidamente extraído, o programa pode ser executado.
# Uso
## 1. Servidor
Ao executar o arquivo JAR, uma interface contendo dois botões irá aparecer. Ao pressionar o botão "Start Server", um servidor local será levantado no endereço http://localhost:8080/, a partir daí será possível executar os HTTP Requests, através do curl, do navegador ou de programas como POSTMAN.
## 2. JSON
A API consome e produz arquivos do tipo JSON. Dentro da pasta /target/json-examples contém arquivos json com os formatos necessários para a API funcionar.
## 3. Artigos
Classe que simula um artigo de blog. Contém os seguintes atributos:
* ID ( Gerado pelo banco de dados)
* Título
* Subtítulo
* Permalink ( Gerado a partir do título )
* Conteúdo
* Data de publicação ( Gerada pelo banco de dados )
* Data da última atualização ( Gerada pelo banco de dados )
* Autores
* Comentários

#### 3.1 GET Request (Único)

Requisição do tipo GET, que busca apenas um artigo no banco de dados.
A busca deve ser feita utilizando o permalink do artigo.
    
    curl --request GET --url http://localhost:8080/artigos/{permalink}

#### 3.2 GET Request (Listagem)

Requisição do tipo GET, que lista os artigos.
A lista contém um sistema de paginação com a possibilidade de escolher a quantidade de elementos por página.

    curl --request GET --url http://localhost:8080/artigos?page=1&size=5

Valores padrões dos parâmetros:
* page = 1
* size = 5

#### 3.3 POST Request
Requisição do tipo POST, para inserção de um novo artigo no banco de dados.

    curl --request POST --url http://localhost:8080/artigos/

Junto da requisição, é necessário enviar um arquivo do tipo JSON.
A inserção deve seguir as seguintes regras para funcionar:
* O ID não deve ser enviado.
* Título não pode ser nulo.
* Subtítulo não pode ser nulo.
* Conteúdo não pode ser nulo.
* Permalink é gerado automaticamente, e não pode ser enviado.
* O artigo precisa de pelo menos um autor.
* Todos os autores precisam ter id e devem já exixtir no banco.
* Artigos não podem ser criados com comentários.

#### 3.4 PUT Request
Requisição do tipo PUT, para atualizar um artigo no banco de dados.

    curl --request PUT --url http://localhost:8080/artigos/

A requisição deve enviar um arquivo JSON com o ID do artigo que será atualizado e com os dados que serão modificados.

#### 3.5 DELETE Request
Requisição do tipo DELETE, para apagar um artigo do banco de dados.

Opção 1 - Selecionando pelo permalink:

    curl --request DELETE --url http://localhost:8080/artigos/{permalink}
Opção 2 - Selecionando pelo ID:

    curl --request DELETE --url 'http://localhost:8080/artigos?id=1'

Basta utilizar uma das duas opções de URL, que o arquivo será deletado.
## 4. Autores
Classe que simula um autor de artigos. Contém os seguintes atributos:
* ID ( Gerado pelo banco de dados )
* Nome
* Sobrenome
* Bio

#### 4.1 GET Request (Único)
Requisição do tipo GET, que busca um autor no banco de dados.

    curl --request GET --url http://localhost:8080/autores/{id}
O autor será buscado através de seu id.
#### 4.2 GET Request (Listagem)

Requisição do tipo GET, que lista os autores.
A lista contém um sistema de paginação com a possibilidade de escolher a quantidade de elementos por página.

    curl --request GET --url http://localhost:8080/autores?page=1&size=5

Valores padrões dos parâmetros:
* page = 1
* size = 5

#### 4.3 POST Request
Requisição do tipo POST, que insere um autor no banco de dados.

    curl --request POST --url http://localhost:8080/autores/

Junto da requisição, é necessário enviar um arquivo do tipo JSON.
A inserção deve seguir as seguintes regras para funcionar:
* O ID não deve ser enviado.
* Nome não pode ser nulo.
* Sobrenome não pode ser nulo.

#### 4.4 PUT Request
Requisição do tipo PUT, para atualizar um autor no banco de dados.

    curl --request PUT --url http://localhost:8080/autores/

A requisição deve enviar um arquivo JSON com o ID do autor que será atualizado e com os dados que serão modificados.

#### 4.5 DELETE Request
Requisição do tipo DELETE, que apaga um autor no banco de dados.

    curl --request DELETE --url http://localhost:8080/autores/{id}
Para deletar, é utilizado o id do autor.

#
## 5. Comentários
Classe que simula o comentário de artigos. Contém os seguintes atributos:
* ID ( Gerado pelo banco de dados )
* ID do Artigo
* Usuário
* Texto
* Data

#### 5.1 GET Request (Único)
Requisição que busca um comentário no banco de dados.

    curl --request GET --url http://localhost:8080/comentarios/{id}
O comentário deve ser buscado pelo seu id.
#### 5.2 GET Request (Listagem)
Requisição que lista os comentários, com opções de paginação.

Opção 1 - Todos os comentários:
    
    curl --request GET --url http://localhost:8080/comentarios?page=1&size=5
Opção 2 - Comentários de um artigo:

    curl --request GET --url http://localhost:8080/artigos/{permalink}/comentarios?page=1&size=5
A segunda opção, utiliza o permalink para selecionar os comentários de um artigo específico.

Valores padrões dos parâmetros:
* page = 1
* size = 5

#### 5.3 POST Request
Requisição para inserção de um comentário no banco de dados.

    curl --request POST --url http://localhost:8080/comentarios/

Junto da requisição, é necessário enviar um arquivo do tipo JSON.
A inserção deve seguir as seguintes regras para funcionar:
* ID do comentário deve ser nulo.
* ID do Artigo não pode ser nulo, e o artigo precisa existir no banco.
* Usuário não pode ser nulo.
* Texto não pode ser nulo.

#### 5.4 PUT Request
Requisição do tipo PUT, para atualizar um comentário no banco de dados.

    curl --request PUT --url http://localhost:8080/comentarios/
A requisição deve enviar um arquivo JSON com o ID do comentário que será atualizado e com os dados que serão modificados.

#### 5.5 DELETE Request
Requisição do tipo DELETE, para remover um comentário do banco de dados.

    curl --request DELETE --url http://localhost:8080/comentarios/{id}
    
O DELETE deve ser feito utilizando o id do comentário.

##  6. Respostas HTTP
* Com exceção dos métodos POST, todas as requisições esperam uma resposta HTTP 200.
* Os métodos POST retornam uma resposta HTTP 201, com o link de acesso para o elemento inserido no banco de dados.
* Em caso de erros, todos os métodos devolvem uma resposta HTTP 500. 
