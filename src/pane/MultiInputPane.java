package pane;

import javax.swing.*;

/*
 * Essa classe � utilizada para receber m�ltiplas informa��es do usu�rio
 */

public class MultiInputPane extends Pane {

	private static final long serialVersionUID = 1L;
	
	//Vari�veis internas
	private static SmartField[] edits;
	
	// Construtor
	public MultiInputPane(String titulo, String texto, Object[][] inputs, int width) {
	
		// Construtor do pane
		super(titulo, texto, width);

		// Re-setando vari�vel width
		width = (int) getBounds().getWidth();

		adicionarEdits(this, inputs);

		terminarJanela(width, inputs.length);
	}
	
	//M�todos
	public void desenharPlaceholders() {
		
		for (SmartField edit : edits) {
			edit.desenharPlaceholder();
		}
	}
	
	public static Object[] input(String titulo, String texto, Object[][]inputs, int width) {

		//Cria uma janela de acordo com as configura��es espec�ficadas
		MultiInputPane pane = new MultiInputPane(titulo, texto, inputs, width);
		
		//Eperando janela renderizar
		try {
			Thread.sleep(100);
		} catch(Exception e) {}
		
		//Desenhando placeholders
		pane.desenharPlaceholders();
		
		Object[] vetor = coletarDados(pane, inputs.length, inputs);
		
		//Retorna o vetor
		return vetor;
	}
	
	private static Object[] coletarDados(MultiInputPane pane, int length, Object[][] inputs) {
		
		//Vetor que ser� retornado
		Object[] vetor = new Object[length];
		
		//La�o para verificar se nenhum edit ficou vazio
		boolean algoVazio = true;
		while (algoVazio) {
			
			algoVazio = false;
			pane.setVisible(true);
			pane.setEstado(0);
			
			//Espera o usu�rio terminar de inserir suas informa��es
			esperaTerminar(pane);
			
			//Coleta as informa��es inseridas
			for (int i = 0; i < length; i++) {
				
				//Vari�vel que recebe a informa��o no edit
				String text = edits[i].getText();
				
				algoVazio = verificarEdit(text, edits[i]);
				
				vetor[i] = retornarValor(text, inputs[i][1].toString());
				
			}
		}
		
		return vetor;
	}
	
	private static Object retornarValor(String text, String tipo) {
		
		//Verificando se � necess�rio converter
		switch (tipo) {
		
			//Caso n�o precise apenas retorna um string
			case "STRING":
				return text;
			
			//Retorna Integer caso necess�rio
			case "INT":
				return Integer.parseInt(text);
			
			//Retorna Double caso necess�rio
			case "DOUBLE":
				return Double.parseDouble(text);
		}
		
		//Caso n�o seja de nenhum tipo aceito retorna nulo
		return null;
	}

	private static boolean verificarEdit(String text, SmartField edit) {
		
		edit.resetCores();
		
		//Verifica se o campo est� vazio
		if (text.isEmpty()) {
			
			edit.sinalizarErro();
			return true;
		}
		
		return false;
	}

	private void adicionarEdits(MultiInputPane pane, Object[][] inputs) {
		
		// Iniciando array
		edits = new SmartField[inputs.length];

		//Adicionando edits � janela
		for (int i = 0; i < inputs.length; i++) {

			//Obtendo caracteres permitidos
			String caracteresPermitidos = "";
					
			iniciarEdit(i, inputs);
					
			SmartField edit = edits[i];

			//Configurando posi��o do texto
			edit.setHorizontalAlignment(SwingConstants.CENTER);
					
			//Arrumando posi��o e tamanho do edit
			edit.setBounds(50, 35 * (i + 1) + lblHeight, width - 100, 25);

			//Adicionando edit � janela
			pane.add(edit);
		}
	}
	
	private void iniciarEdit(int i, Object[][] inputs) {
		
		//Verificando tipo
		switch (inputs[i][1].toString().toUpperCase()) {
			
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
			
			default: {
				
				boolean decimal = inputs[i][1].toString().equals("DOUBLE") ? true : false;
				
				//Verificando se foi passado m�nimo e m�ximo para esse edit
				if (inputs[i].length > 2) {
					
					//Atribuindo valores
					double min = (double) inputs[i][2];
					double max = (double) inputs[i][3];
					
					//Iniciando edit com limites personalizados
					edits[i] = new SmartField(decimal, min, max);
				} else {
					
					//Iniciando edit padr�o
					edits[i] = new SmartField(decimal);
				}
				break;
			}
		}
	}

	private void terminarJanela(int width, int ilength) {
		
		//Adicionando bot�o para finalizar
		add(new FinishButton(this, width, 35 * (ilength + 1) + lblHeight));
				
		//Arrumando tamanho da tela (75 seria o tamanho do bot�o mais 50 de margem)
		setBounds(0, 0, width,  35 * (ilength + 1) + lblHeight + 75);

		// Seta para a janela aparecer no meio da tela
		setLocationRelativeTo(null);

		// Deixa a janela vis�vel
		setVisible(true);
	}
}