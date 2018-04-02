Por forma a simplificar a compilação do projeto, foi criado um script.
Para tal basta correr, na pasta 'scripts', o comando:
		sh build.sh

		
IMP: Para assegurar que o projeto e os scripts correm como esperado,
a estrutura das pastas 'src', 'scripts' e 'bin' deve ser mantida sendo
que estas devem estar colocadas na mesma pasta. O script assume que o
compilador javac se encontra no path do sistema.

Para executar as aplicações sugere-se o uso dos scripts fornecidos (e
cuja documentação se encontra em Scripts_Info.txt), mas se for necessário
saber quais os argumentos de cada app basta executá-las sem argumentos
para obter a "usage". A app de teste tem como ponto de entrada o .class
testApp.TestApp e o peer tem como ponto de entrada peer.Main