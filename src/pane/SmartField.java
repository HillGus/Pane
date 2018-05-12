package pane;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.BorderFactory;
import javax.swing.JTextField;

/*
 * Essa Classe � utilizada para ter um controle maior sobre o que o usu�rio informa na janela de di�logo
 */

public class SmartField extends JTextField {
	
	private static final long serialVersionUID = 1L;
	private String caracteresPermitidos, placeholder, tipo;
	private double min, max;
	private int maxLength;
	
	//Construtor simples
	public SmartField() {
		
		this(null, "", 0);
	}
	
	//Construtor com caracteres permitidos personalizados
	public SmartField(String caracteresPermitidos) {
		
		this(null, caracteresPermitidos, 0);
	}

	//Construtor com caracteres permitidos e tamanho m�ximo
	public SmartField(String caracteresPermitidos, int maxLength) {
		
		this(null, caracteresPermitidos, maxLength);
	}
	
	//Construtor com placeholder e caracteres permitidos personalizados
	public SmartField(String placeholder, String caracteresPermitidos) {
		
		this(placeholder, caracteresPermitidos, 0);
	}
	
	//Construtor com placeholder, caracteres permitidos e tamanho m�ximo personalizados
	public SmartField(String placeholder, String caracteresPermitidos, int maxLength) {
			
		//Construtor do JTextField
		super();
			
		//Definindo placeholder
		this.placeholder = placeholder;
			
		//Definindo conjunto de caracteres permitidos
		this.caracteresPermitidos = caracteresPermitidos;
		
		//Definindo tamanho m�ximo
		this.maxLength = maxLength;
		System.out.println(maxLength);
		
		//Definindo tipo
		this.tipo = "STRING";
	}
	
	//Construtor simples para inputs num�rios
	public SmartField(boolean decimal) {
		
		this(decimal, decimal ? Pane.DOUBLE_MIN : Pane.INT_MIN, decimal ? Pane.DOUBLE_MAX : Pane.INT_MAX);
	}
	
	//Construtor para inputs num�ricos com placeholder
	public SmartField(boolean decimal, String placeholder) {
		
		this(decimal, placeholder, decimal ? Pane.DOUBLE_MAX : Pane.INT_MAX, decimal ? Pane.DOUBLE_MIN : Pane.INT_MIN);
	}
	
	//Construtor para inputs num�ricos com limites
	public SmartField(boolean decimal, double min, double max) {
		
		this(decimal, null, min, max);
	}

	//Construtor para inputs num�ricos com limites e placeholder
	public SmartField(boolean decimal, String placeholder, double min, double max) {
		
		//Construtor do JTextField
		super();
					
		//Definindo tipo
		this.tipo = decimal ? "DOUBLE" : "INT";
				
		//Definindo m�nimo e m�ximo
		this.min = min;
		this.max = max;
		
		//Definindo placeholder
		this.placeholder = placeholder;
	}
	
	//Sobrescrevendo m�todo que trata os eventos acionados por teclas
	@Override
	protected void processKeyEvent(KeyEvent e) {
		
		atualizarListeners(e);
		
		char key = e.getKeyChar();
		
		//Definindo qual tipo de evento foi acionado
		switch (e.getID()) {
			
			case KeyEvent.KEY_TYPED: {	
				
				//Verificando qual tecla foi digitada
				if (key == KeyEvent.VK_BACK_SPACE) {
					
					//Limpa a �ltima casa do texto caso necess�rio
					if (getText().length() > 0) {
						setText(getText().substring(0, getText().length() - 1));
					}
					
				//Adiciona o caractere ao TextField caso esteja na lista de caracteres permitidos
				//Caso a lista esteja vazia, o m�todo entende que podem ser colocadas quaisquer caracteres
				} else {
				
					//Verificando tipo do TextField
					if (!tipo.equals("STRING")) {
						
						processarTiposNumericos(String.valueOf(e.getKeyChar()));
						
					} else {
						
						//Verificando se o caractere � permitido
						if ((caracteresPermitidos.contains(String.valueOf(key))) || (caracteresPermitidos.equals(""))) {
							
							//Verificando se o length m�ximo ainda n�o foi atingido ou se � livre
							if ((getText().length() < maxLength) || (maxLength == 0)) {
								
								setText(getText() + key);
							}
						}
					}
				}
				
				//Verifica se deve desenhar o placeholder
				if (getText().isEmpty()) {
					
					desenharPlaceholder();
				}
			}
		}
	}
	
	//M�todos
	//Apar�ncia
	public void desenharPlaceholder() {
		
		//Verifica se o placeholder existe
		if (this.placeholder == null) {
			return;
		}
		
		//Obtendo componente para desenhar na tela
		Graphics g = getGraphics();
		
		//Definindo fonte do graphics
		g.setFont(new Font("Arial", Font.ITALIC, 12));
		
		//Obtendo largura do edit
		int editWidth = (int) getBounds().getWidth();
		
		//Obtendo largura do texto
		FontMetrics metrics = g.getFontMetrics();
		int widthTexto = metrics.stringWidth(placeholder);
		
		//Calculando coordenadas para o desenho do placeholder
		int x = (editWidth / 2) - (widthTexto / 2), y = 18;
		
		//Desenha o placeholder
		g.setColor(Color.GRAY);
		g.drawString(placeholder, x, y);
	}

	public void sinalizarErro() {
		
		//Define a cor da borda como vermelho
		setBorder(BorderFactory.createLineBorder(Color.RED));
		
		//Define a cor do fundo como vermelho
		setBackground(Color.decode("#ffb3b3"));
		
		//Define a cor do texto como vermelho
		setForeground(Color.RED);
	}

	public void resetCores() {
		
		//Reseta cor da borda
		setBorder(BorderFactory.createLineBorder(Color.GRAY));
		
		//Reseta cor do fundo
		setBackground(Color.WHITE);
		
		//Reseta cor do texto
		setForeground(Color.BLACK);
	}

	//Funcionalidade
	private void atualizarListeners(KeyEvent e) {
		
		//Obtendo lista de keyListeners
		KeyListener[] listeners = getKeyListeners();
		
		for (KeyListener listener : listeners) {
			
			switch(e.getID()) {
			
				case KeyEvent.KEY_PRESSED: {
					
					listener.keyPressed(e);
					break;
				}
				case KeyEvent.KEY_TYPED: {
					
					listener.keyTyped(e);
					break;
				}
				
				case KeyEvent.KEY_RELEASED: {
					
					listener.keyReleased(e);
					break;
				}
			}
		}
	}

	private void processarTiposNumericos(String key) {
		
		//Verificando se o campo est� preenchido com um 0
		if (getText().equals("0")) {
			
			//Limpa o campo
			setText("");
		}
		
		//Verificando se o caractere � um n�mero
		if ("0123456789".contains(key)) {
			
			//Verifica de qual tipo � o SmartField
			if (tipo.equals("DOUBLE")) {
				
				verificarLengthPosVirgula(key);
			} else {
				setText(getText() + key);
			}
		} else {
			
			processarSimbolos(key);
		}
		
		clampNumericos(key);
	}
	
	private void verificarLengthPosVirgula(String key) {
		
		int index = getText().indexOf(".");
		
		//Verifica se j� existe um . no texto
		if (index != -1) {
			
			//Verifica se o tamanho m�ximo ainda n�o foi atingido
			if ((getText().length() - (index + 1)) < maxLength) {
				setText(getText() + key);
			}
		}
	}
	
	private void processarSimbolos(String key) {
		
		//Verificando se o s�mbolo j� est� no TextField
		if (!getText().contains(key)) {
			
			//Verificando se o s�mbolo � o sinal de menos
			if (key.charAt(0) == KeyEvent.VK_MINUS) {
				
				//Verificando o TextField est� vazio
				if (getText().length() == 0) {
					
					setText(getText() + key);
				}
			} else {
				
				setText(getText() + key);
			}
		}
	}
	
	private void clampNumericos(String key) {
		
		//Tenta clampar o valor inserido
		try {
			
			if (tipo.equals("DOUBLE")) {
				clampDouble();
			} else {
				clampInt();
			}
			
		} catch (Exception ex) {
			
			//Define s�mbolos permitidos
			String simbolosPermitidos = tipo.equals("DOUBLE") ? "-." : "-";
			
			//Verifica se o caractere inserido n�o est� nos s�mbolos permitidos
			if (!simbolosPermitidos.contains(key)) {
				setText("0");
			}
			
		}
	}
	
	private void clampInt() {
		
		int n = Integer.parseInt(getText());
		
		//Verifica se o n�mero est� abaixo do m�nimo aceito
		if (n < min) {
			
			//Troca o conte�do para o m�nimo aceito
			setText(String.valueOf((int)min));
		}
		
		//Verifica se o n�mero est� acima do m�ximo aceito
		if (n > max) {
			
			//Troca o conte�do para o m�ximo aceito
			setText(String.valueOf((int)max));
		}
	}

	private void clampDouble() {
		
		double n = Double.parseDouble(getText());
		
		//Verifica se o n�mero est� abaixo do m�nimo aceito
		if (n < min) {
			
			//Troca o conte�do para o m�nimo aceito
			setText(String.valueOf(min));
		}
		
		//Verifica se o n�mero est� acima do m�ximo aceito
		if (n > max) {
			
			//Troca o conte�do para o m�ximo aceito
			setText(String.valueOf(max));
		}
	}
	
	//Getters
	public double getDoubleText() {
		
		return Double.parseDouble(getText());
	}
	
	public int getIntText() {
		
		return Integer.parseInt(getText());
	}
}