package pane;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JTextField;

/*
 * Essa Classe � utilizada para ter um controle maior sobre o que o usu�rio informa na janela de di�logo
 */

public class SmartField extends JTextField {
	
	private static final long serialVersionUID = 1L;
	private ArrayList<Integer> caracteresPressionados = new ArrayList<>();
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
		
		//Definindo tipo
		this.tipo = "STRING";
	}
	
	//Construtor simples para inputs num�rios
	public SmartField(boolean decimal) {
		
		this(decimal, 0, 0);
	}
		
	//Construtor para inputs num�ricos com limite p�s v�rgula
	public SmartField(boolean decimal, int maxLength) {
		
		this(decimal, null, maxLength);
	}
	
	//Construtor para inputs num�ricos com placeholder
	public SmartField(boolean decimal, String placeholder) {
		
		this(decimal, placeholder, 0, 0, 0);
	}
	
	//Construtor para inputs num�ricos com placeholder e limite p�s v�rgula
	public SmartField(boolean decimal, String placeholder, int maxLength) {
		
		this (decimal, placeholder, 0, 0, maxLength);
	}
	
	//Construtor para inputs num�ricos com limites
	public SmartField(boolean decimal, double min, double max) {
		
		this(decimal, null, min, max, 0);
	}

	//Construtor para inputs num�ricos com limites e limite p�s v�rgula
	public SmartField(boolean decimal, double min, double max, int maxLength) {
		
		this(decimal, null, min, max, maxLength);
	}
	
	//Construtor para inputs num�ricos com limites e placeholder
	public SmartField(boolean decimal, String placeholder, double min, double max, int maxLength) {
		
		//Construtor do JTextField
		super();
					
		//Definindo tipo
		this.tipo = decimal ? "DOUBLE" : "INT";
				
		//Definindo m�nimo e m�ximo
		this.min = min;
		this.max = max;
		
		//Definindo tamanho m�ximo p�s v�rgula
		this.maxLength = maxLength;
		
		//Definindo placeholder
		this.placeholder = placeholder;
	
		//Definindo valor inicial do texto
		if (decimal) {
			setText(String.valueOf(min));
		} else {
			setText(String.valueOf((int)min));
		}
		
		//Adicionando administrador de foco
		addFocusListener(new FocusListener() {
			
			@Override
			public void focusLost(FocusEvent e) {
				
				try {
					
					//Verificando se o tipo � num�rico
					if (!tipo.equals("STRING")) {
						
						//Verificando se � decimal ou n�o
						if (tipo.equals("DOUBLE")) {
							
							//Clampa
							clampDouble();
						} else {
							
							//Clampa
							clampInt();
						}
					}
					
				} catch(Exception ex) {}
			}
			
			@Override
			public void focusGained(FocusEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
	
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
	//Sobrescrevendo m�todo que trata os eventos acionados por teclas
	@Override
	protected void processKeyEvent(KeyEvent e) {
		
		atualizarListeners(e);
		
		if (caracteresPressionados.size() > 1) {
			multiplasTeclas();
		}
		
		char key = e.getKeyChar();
		
		if (e.getID() == KeyEvent.KEY_TYPED ) {
			
			//Verificando qual tecla foi digitada
			if (key == KeyEvent.VK_BACK_SPACE) {
				
				processarBackSpace();
				
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

	private void atualizarListeners(KeyEvent e) {
		
		//Obtendo lista de keyListeners
		KeyListener[] listeners = getKeyListeners();
		
		for (KeyListener listener : listeners) {
			
			switch(e.getID()) {
			
				case KeyEvent.KEY_PRESSED: {
					
					if (!caracteresPressionados.contains(e.getKeyCode())) {
						caracteresPressionados.add(e.getKeyCode());
					}
					
					listener.keyPressed(e);
					break;
				}
				case KeyEvent.KEY_TYPED: {
					
					listener.keyTyped(e);
					break;
				}
				
				case KeyEvent.KEY_RELEASED: {
					
					if (caracteresPressionados.contains(e.getKeyCode())) {
						
						int index = indexCaracter(e.getKeyCode());
						
						if (index > -1) {
							caracteresPressionados.remove(index);
						}
					}
					
					listener.keyReleased(e);
					break;
				}
			}
		}
	}

	private int indexCaracter(int ch) {
		
		for (int caractere : caracteresPressionados) {
			
			if (caractere == ch) {
				
				return caracteresPressionados.indexOf(caractere);
			}
		}
		
		return -1;
	}
	
	private void multiplasTeclas() {
		
		switch (caracteresPressionados.get(0)) {
		
			case KeyEvent.VK_CONTROL: {
				
				switch (caracteresPressionados.get(1)) {
				
					case KeyEvent.VK_A: {

						selectAll();
					}
				}
			}
		}
		
	}
	
	private void processarBackSpace() {
		
		//Verificando se n�o h� nenhum texto selecionado
		if (getSelectedText() == null) {
			
			//Verifica se existe algo escrito
			if (getText().length() > 0) {
				
				//Retira �ltima casa do texto atual
				String novoTexto = getText().substring(0, getText().length() - 1);
				setText(novoTexto);
			}
		} else {
			
			retirarDoTexto(getSelectedText());
		}
	}

	private void retirarDoTexto(String texto) {
		
		//Encontra o come�o e fim do texto
		int[] bounds = encontrarNoTexto(texto, getCaretPosition());
		
		//Separa o conte�do do campo no texto
		String parte1 = getText().substring(0, bounds[0]);
		String parte2 = getText().substring(bounds[1], getText().length());
			
		//Seta texto
		setText(parte1 + parte2);
			
		//Seta posi��o da barra de texto
		setCaretPosition(parte1.length());
	}
	
	private int[] encontrarNoTexto(String texto, int caretPos) {
		
		int[] bounds = new int[2];
		
		//Calcula o come�o e o fim do texto selecionado
		if (caretPos - texto.length() >= 0) {
			
			bounds[0] = caretPos - texto.length();
			bounds[1] = caretPos;
		} else {
			
			bounds[0] = caretPos;
			bounds[1] = caretPos + texto.length();
		}
		
		//Retorno
		return bounds;
		
	}
	
	private void processarTiposNumericos(String key) {
		
		//Verificando se o caractere � um n�mero
		if ("0123456789".contains(key)) {
			
			//Verifica se o texto � 0
			if (getText().equals("0")) {
			
				setText("");
			}	
			
			//Verifica de qual tipo � o SmartField
			if (tipo.equals("DOUBLE")) {
				
				verificarLengthPosVirgula(key);
			} else {
				
				//Verifica se o texto inserido no campo � igual ao m�nimo
				if (!getText().equals(String.valueOf((int)min))) {
					
					setText(getText() + key);
				} else {
					
					setText(key);
				}
				
			}
		} else {
			
			processarSimbolos(key);
		}
		
		clampNumericos(key);
	}
	
	private void verificarLengthPosVirgula(String key) {
		
		int index = getText().indexOf(".");
		
		//Verifica se j� existe um . no texto e se existe um limite
		//O objeto entende que 0 � sem limite
		if ((index > -1) && (maxLength > 0)) {
			
			//Verifica se o tamanho m�ximo ainda n�o foi atingido
			if ((getText().length() - (index + 1)) < maxLength) {
				setText(getText() + key);
			}
		} else {
			
			//Verifica se o texto inserido no campo � igual ao m�nimo
			if (!getText().equals(String.valueOf(min))) {
				
				setText(getText() + key);
			} else {
				
				setText(key);
			}
		}
	}
	
	private void processarSimbolos(String key) {
		
		//Define s�mbolos permitidos
		String simbolosPermitidos = this.tipo.equals("DOUBLE") ? "-." : "-";
		
		//Verificando se o s�mbolo j� est� no TextField e se � s�mbolo permitido
		if ((!getText().contains(key)) && (simbolosPermitidos.contains(key))) {
			
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
			
			//Verificando se a �ltima letra digitada � um ponto
			if (key.equals(".")) {
				return;
			}
			
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
		
		//Verifica se precisa ajustar
		if (min != max) {
			
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
	}

	private void clampDouble() {
		
		double n = Double.parseDouble(getText());
		
		//Verifica se precisa ajustar
		if (min != max) {
			
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
	}
	
	//Getters e Setters
	public double getDouble() {
		
		clampDouble();
		
		return Double.parseDouble(getText());
	}
	
	public int getInt() {
		
		clampInt();
		
		return Integer.parseInt(getText());
	}
}