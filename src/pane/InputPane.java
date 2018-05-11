package pane;

import java.awt.Rectangle;

import javax.swing.*;

/*
 * Essa classe � a usada para receber uma �nica informa��o do usu�rio
 */

public class InputPane extends Pane {

	private SmartField edit;

	//Construtor simples
	public InputPane(String titulo, String texto) {
		
		this(titulo, texto, "");
	}
	
	//Construtor com caracteres permitidos personalizados
	public InputPane(String titulo, String texto, String caracteresPermitidos) {
		
		this(titulo, texto, caracteresPermitidos, Pane.DEFAULT_SIZE);
	}
	
	//Construtor com caracteres permitidos e largura personalizados
	public InputPane(String titulo, String texto, String caracteresPermitidos, int width) {
		
		// Construtor do pane
		super(titulo, texto, width);

		// Re-setando vari�vel width
		this.width = (int) getBounds().getWidth();
		
		//Iniciando edit
		edit = new SmartField(caracteresPermitidos);
		
		terminarJanela();
	}
	
	//Construtor num�rico simples
	public InputPane(String titulo, String texto, boolean decimal) {
		
		this(titulo, texto, Pane.DEFAULT_SIZE, decimal);
	}
	
	//Construtor num�rico com largura personalizada
	public InputPane(String titulo, String texto, int width, boolean decimal) {
		
		//Construtor do pane
		super(titulo, texto, width);
		
		//Re-setando vari�vel width
		this.width = (int) getBounds().getWidth();
		
		//Iniciando edit
		edit = new SmartField(decimal);
		
		terminarJanela();
	}
	
	//Construtor num�rico com limites personalizados
	public InputPane(String titulo, String texto, boolean decimal, double min, double max) {
		
		this(titulo, texto, Pane.DEFAULT_SIZE, decimal, min, max);
	}
	
	//Construtor num�rico com limites e largura personalizados
	public InputPane(String titulo, String texto, int width, boolean decimal, double min, double max ) {
		
		//Construtor do pane
		super(titulo, texto, width);
		
		//Re-setando vari�vel width
		this.width = (int) getBounds().getWidth();
		
		//Iniciando edit
		edit = new SmartField(decimal, min, max);
		
		terminarJanela();
	}
	
	// Termina de construir a janela
	private void terminarJanela() {
		
		// Arruma a posi��o do texto dentro do edit
		edit.setHorizontalAlignment(SwingConstants.CENTER);

		// Arruma a posi��o do campo de entrada
		edit.setBounds(new Rectangle(50, 35 + lblHeight, width - 100, 25));

		// Adiciona um KeyAdapter para "ouvir" as teclas inseridas
		edit.addKeyListener(new KeyAdap(this));

		// Adiciona o campo de entrada � janela
		add(edit);

		// Adiciona um bot�o que finaliza a atividade � janela
		add(new FinishButton(this, width, 70 + lblHeight));

		// Arruma o tamanho da janela (145 seria a margem superior do label, o tamanho e margem do edit e do bot�o)
		setBounds(0, 0, width, 145 + lblHeight);

		// Seta para a janela aparecer no meio da tela
		setLocationRelativeTo(null);

		// Deixa a janela vis�vel
		setVisible(true);

	}

	// M�todos
	public static String input(String titulo, String texto) {
		
		//Cria janela
		InputPane pane = new InputPane(titulo, texto);
		
		//Retorna valor inserido na janela
		return input(pane);
	}
	
	public static String input(String titulo, String texto, String caracteresPermitidos) {
	
		//Cria janela
		InputPane pane = new InputPane(titulo, texto, caracteresPermitidos);
		
		//Retorna o valor inserido na janela
		return input(pane);
	}
	
	public static String input(String titulo, String texto, String caracteresPermitidos, int width) {
		
		//Cria a janela
		InputPane pane = new InputPane(titulo, texto, caracteresPermitidos, width);
		
		//Retorna o valor inserido na janela
		return input(pane);
	}
	
	public static Object input(String titulo, String texto, boolean decimal) {
		
		//Cria a janela
		InputPane pane = new InputPane(titulo, texto, decimal);
		
		//Retorna o valor inserido na janela j� convertido
		if (decimal) {
			return Double.parseDouble(input(pane));
		} else {
			return Integer.parseInt(input(pane));
		}
	}
	
	public static Object input(String titulo, String texto, boolean decimal, double min, double max) {
		
		//Cria uma janela
		InputPane pane = new InputPane(titulo, texto, decimal, min, max);
		
		//Retorna o valor inserido na janela j� convertido
		if (decimal) {
			return Double.parseDouble(input(pane));
		} else {
			return Integer.parseInt(input(pane));
		}
	}
	
	public static Object input(String titulo, String texto, int width, boolean decimal) {
		
		//Cria a janela
		InputPane pane = new InputPane(titulo, texto, width, decimal);
		
		//Retorna o valor inserido na janela j� convertido
		if (decimal) {
			return Double.parseDouble(input(pane));
		} else {
			return Integer.parseInt(input(pane));
		}
	}
	
	public static Object input(String titulo, String texto, int width, boolean decimal, double min, double max) {
		
		//Cria uma janela
		InputPane pane = new InputPane(titulo, texto, width, decimal, min, max);
		
		//Retorna o valor inserido na janela j� convertido
		if (decimal) {
			return Double.parseDouble(input(pane));
		} else {
			return Integer.parseInt(input(pane));
		}
	}
	
	public static String input(InputPane pane) {
		
		//Espera o usu�rio terminar de inserir suas informa��es
		esperaTerminar(pane);

		// Retorna as informa��es passadas pela janela
		return pane.edit.getText();
	}

	// Getters e Setters
	public JTextField getEdit() {
		return this.edit;
	}
}