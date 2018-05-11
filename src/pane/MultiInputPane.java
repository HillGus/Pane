package pane;

import java.awt.Color;
import java.awt.Font;

import javax.swing.*;

/*
 * Essa classe � utilizada para receber m�ltiplas informa��es do usu�rio
 */

public class MultiInputPane extends Pane {

	//Vari�veis internas
	private static SmartField[] edits;
	
	// Construtor
	public MultiInputPane(String titulo, String texto, Object[][] inputs, int width) {
	
		// Construtor do pane
		super(titulo, texto, width);

		// Re-setando vari�vel width
		width = getBounds().width;

		// Iniciando array
		edits = new SmartField[inputs.length];

		//Adicionando edits � janela
		for (int i = 0; i < inputs.length; i++) {

			//Obtendo caracteres permitidos
			String caracteresPermitidos = "";
			
			//Obtendo m�nimo e m�ximo para o TextField
			double min = Pane.DOUBLE_MIN, max = Pane.DOUBLE_MAX;
			
			switch (inputs[i][1].toString().toUpperCase()) {
			
				case "INT": {
					
					//Verificando se foi passado m�nimo e m�ximo para esse edit
					if (inputs[i].length > 2) {
						
						//Atribuindo valores
						min = (double) (Integer) inputs[i][2];
						max = (double) (Integer) inputs[i][3];
						
						//Iniciando edit com valores personalizados
						edits[i] = new SmartField(false, min, max);
					} else {
					
						//Iniciando edit padr�o
						edits[i] = new SmartField(false);
					}
					break;
				}
				
				case "DOUBLE": {
					
					//Verificando se foi passado m�nimo e m�ximo para esse edit
					if (inputs[i].length > 2) {
						
						//Atribuindo valores
						min = (double) (Integer) inputs[i][2];
						max = (double) (Integer) inputs[i][3];
						
						//Iniciando edit com limites personalizados
						edits[i] = new SmartField(true, min, max);
					} else {
						
						//Iniciando edit padr�o
						edits[i] = new SmartField(true);
					}
					break;
				}
				
				case "STRING": {
					
					//Verificando se foi passado uma par�metro extra
					if (inputs[i].length > 2) {
						
						//Iniciando edit com caracteres permitidos personalizados
						edits[i] = new SmartField(inputs[i][0].toString(), inputs[i][2].toString());
					} else {
						
						//Inciando edit padr�o
						edits[i] = new SmartField(inputs[i][0].toString());
					}
				}
			}
			SmartField edit = edits[i];

			//Configurando posi��o do texto
			edit.setHorizontalAlignment(SwingConstants.CENTER);
			
			//Arrumando posi��o e tamanho do edit
			edit.setBounds(50, 35 * (i + 1) + lblHeight, width - 100, 25);

			//Adicionando edit � janela
			add(edit);
		}

		//Adicionando bot�o para finalizar
		add(new FinishButton(this, width, 35 * (inputs.length + 1) + lblHeight));
		
		//Arrumando tamanho da tela (75 seria o tamanho do bot�o mais 50 de margem)
		setBounds(0, 0, width,  35 * (inputs.length + 1) + lblHeight + 75);

		// Seta para a janela aparecer no meio da tela
		setLocationRelativeTo(null);

		// Deixa a janela vis�vel
		setVisible(true);
	}
	
	public void desenharPlaceholders() {
		
		for (SmartField edit : edits) {
			edit.desenharPlaceholder();
		}
	}
	
	public static Object[] input(String titulo, String texto, Object[][]inputs, int width) {
		
		//Vetor que ser� retornado
		Object[] vetor = new Object[inputs.length];

		//Cria uma janela de acordo com as configura��es espec�ficadas
		MultiInputPane pane = new MultiInputPane(titulo, texto, inputs, width);
		
		//Eperando janela renderizar
		try {
			Thread.sleep(100);
		} catch(Exception e) {}
		
		//Desenhando placeholders
		pane.desenharPlaceholders();
		
		//La�o para verificar se nenhum edit ficou vazio
		boolean algoVazio = true;
		while (algoVazio) {
			
			algoVazio = false;
			pane.setVisible(true);
			pane.setEstado(0);
			
			//Espera o usu�rio terminar de inserir suas informa��es
			esperaTerminar(pane);
			
			//Coleta as informa��es inseridas
			for (int i = 0; i < vetor.length; i++) {
				
				//Vari�vel que recebe a informa��o no edit
				String text = edits[i].getText();
				
				//Reseta cores do edit
				edits[i].setBorder(BorderFactory.createLineBorder(Color.GRAY));
				edits[i].setBackground(Color.WHITE);
				edits[i].setForeground(Color.BLACK);
				
				//Verifica se o campo est� vazio
				if (text.isEmpty()) {
					
					//Define a cor da borda como vermelho
					edits[i].setBorder(BorderFactory.createLineBorder(Color.RED));
					
					//Define a cor do fundo como vermelho
					edits[i].setBackground(Color.decode("#ffb3b3"));
					
					//Define a cor do texto como vermelho
					edits[i].setForeground(Color.RED);
					
					algoVazio = true;
				}
				
				//Verificando se � necess�rio converter
				switch (inputs[i][1].toString().toUpperCase()) {
				
					//Caso n�o precise apenas seta a vari�vel como string
					case "STRING":
						vetor[i] = text;
						break;
					
					//Converte para integer caso necess�rio
					case "INT":
						vetor[i] = Integer.parseInt(text);
						break;
					
					//Converte para double caso necess�rio
					case "DOUBLE":
						vetor[i] = Double.parseDouble(text);
						break;
				}
			}
		}
		
		//Retorna o vetor
		return vetor;
	}
}