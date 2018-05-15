package pane;

import java.awt.Rectangle;

import javax.swing.*;

/*
 * Essa classe � a usada para receber uma �nica informa��o do usu�rio
 */

public class InputPane extends Pane {

	private static final long serialVersionUID = 1L;
	
	private SmartField edit;

	//Construtor simples
	public InputPane(String titulo, String texto) {
		
		this(titulo, texto, "");
	}
	
	//Construtor com tamanho m�ximo
	public InputPane(String titulo, String texto, int maxLength) {
		
		this(titulo, texto, "", Pane.RESIZE_AS_NEEDED, maxLength);
	}
	
	//Construtor com caracteres permitidos personalizados
	public InputPane(String titulo, String texto, String caracteresPermitidos) {
		
		this(titulo, texto, caracteresPermitidos, Pane.RESIZE_AS_NEEDED, 0);
	}
	
	//Construtor com caracteres permitidos e tamanho m�ximo personalizados
	public InputPane(String titulo, String texto, String caracteresPermitidos, int maxLength) {
		
		this(titulo, texto, caracteresPermitidos, Pane.RESIZE_AS_NEEDED, maxLength);
	}
	
	//Construtor com caracteres permitidos e largura personalizados
	public InputPane(String titulo, String texto, String caracteresPermitidos, int width, int maxLength) {
		
		// Construtor do pane
		super(titulo, texto, width);

		// Re-setando vari�vel width
		this.width = (int) getBounds().getWidth();
		
		//Iniciando edit
		edit = new SmartField(caracteresPermitidos, maxLength);
		
		terminarJanela();
	}
	
	//Construtor num�rico simples
	public InputPane(String titulo, String texto, boolean decimal) {
		
		this(titulo, texto, Pane.RESIZE_AS_NEEDED, decimal);
	}
	
	//Construtor num�rico com largura personalizada
	public InputPane(String titulo, String texto, int width, boolean decimal) {
		
		this(titulo, texto, width, decimal, 0, 0, 0);
	}
	
	//Construtor num�rico com limites personalizados
	public InputPane(String titulo, String texto, boolean decimal, double min, double max) {
		
		this(titulo, texto, Pane.RESIZE_AS_NEEDED, decimal, min, max, 0);
	}
	
	//Construtor num�rico com limites, largura e tamanho m�ximo p�s v�rgula personalizados
	public InputPane(String titulo, String texto, int width, boolean decimal, double min, double max, int maxLength) {
		
		//Construtor do pane
		super(titulo, texto, width);
		
		//Re-setando vari�vel width
		this.width = (int) getBounds().getWidth();
		
		//Iniciando edit
		edit = new SmartField(decimal, min, max, maxLength);
		
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
		
		//Retorna valor inserido na janela
		return input(titulo, texto, 0);
	}
	
	public static String input(String titulo, String texto, int maxLength) {
		
		//Retorna valor inserido na janela
		return input(titulo, texto, "", maxLength);
	}
	
	public static String input(String titulo, String texto, String caracteresPermitidos) {

		//Retorna o valor inserido na janela
		return input (titulo, texto, caracteresPermitidos, 0);
	}
	
	public static String input(String titulo, String texto, String caracteresPermitidos, int maxLength) {
		
		//Retorna o valor inserido na janela
		return input(titulo, texto, caracteresPermitidos, Pane.RESIZE_AS_NEEDED, maxLength);
	}
	
	public static String input(String titulo, String texto, String caracteresPermitidos, int width, int maxLength) {
		
		//Cria a janela
		InputPane pane = new InputPane(titulo, texto, caracteresPermitidos, width, maxLength);
		
		//Espera o usu�rio terminar de usar a janela
		esperaTerminar(pane);
		
		//Retorna o valor inserido na janela
		return pane.getEdit().getText();
	}
	
	public static Object input(String titulo, String texto, boolean decimal) {
		
		//Retorna o valor inserido na janela j� convertido
		return input(titulo, texto, decimal, 0, 0);
	}
	
	public static Object input(String titulo, String texto, boolean decimal, int maxLength) {
		
		//Retorna o valor inserido na janela j� convertido
		return input(titulo, texto, Pane.RESIZE_AS_NEEDED, decimal, 0, 0, maxLength);
	}
	
	public static Object input(String titulo, String texto, boolean decimal, double min, double max) {
		
		//Retorna o valor inserido na janela j� convertido
		return input(titulo, texto, Pane.RESIZE_AS_NEEDED, decimal, min, max, 0);
	}
	
	public static Object input(String titulo, String texto, boolean decimal, double min, double max, int maxLength) {
		
		//Retorna o valor inserido na janela j� convertido
		return input(titulo, texto, Pane.RESIZE_AS_NEEDED, decimal, min, max, maxLength);
	}
	
	public static Object input(String titulo, String texto, int width, boolean decimal) {
		
		//Retorna o valor inserido na janela j� convertido
		return input(titulo, texto, width, decimal, 0, 0, 0);
	}
	
	public static Object input(String titulo, String texto, int width, boolean decimal, double min, double max, int maxLength) {
		
		//Cria uma janela
		InputPane pane = new InputPane(titulo, texto, width, decimal, min, max, maxLength);
		
		//Espera o usu�rio terminar de usar a janela
		esperaTerminar(pane);
		
		//Retorna o valor inserido na janela j� convertido
		if (decimal) {
			return pane.getEdit().getDouble();
		} else {
			return pane.getEdit().getInt();
		}
	}
	
	// Getters e Setters
	private SmartField getEdit() {
		return this.edit;
	}
}