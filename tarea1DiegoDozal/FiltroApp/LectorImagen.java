import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.*; 
import java.io.*;
import javax.imageio.ImageIO;

/**
 * Clase que modela a un LectorImagen, se encarga de leer la imagen cargada por el usuario y 
 * aplica los filtros dependiendo de la entrada del usuario 
 */
class LectorImagen {

        private BufferedImage imagenFiltrada; 
        private int ancho; 
        private int alto;
        
        public LectorImagen(String path){
            try {
                File input = new File(path);
                imagenFiltrada= ImageIO.read(input);
                ancho = imagenFiltrada.getWidth();
                alto = imagenFiltrada.getHeight();      
             } catch (Exception e) {}  
        }
        
        /**
         * Metodo para aplicar un filtro de gris dada la entrada del usuario 
         * @param tipo
         */
        public void filtro_gris_(int tipo){

            try{
                for(int i=0; i<ancho; i++) {
                    
                    for(int j=0; j<alto; j++) {
                    
                       //Obtiene el valor de cada pixel 
                       int pixel = imagenFiltrada.getRGB(i, j);
                      // Generamos el color que rellenara a cada pixel 
                       Color color = new Color(pixel,true);
                      //Obtenemos los colores de ese pixel 
                       int r = color.getRed(); 
                       int g = color.getGreen(); 
                       int b = color.getBlue(); 
                       // Se generan los nuevos valores para el color utiizando la funcion generaGris 
                       int gray = this.generaGris(tipo, r, g, b);  
                       
    
                       //Se genera un nuevo color usando los generados 
                       color = new Color(gray,gray, gray);
                       imagenFiltrada.setRGB(i, j, color.getRGB());
                     
                    }
                 }
                }
                catch(Exception e){}

        }

        /**
         * Metodo para generar un valor en entero dado el tipo de gris que el usuario desea,
         * y los 3 colores 
         * @param tipo
         * @param red
         * @param green
         * @param blue
         * @return
         */
        public int generaGris(int tipo, int red, int green, int blue){
            switch(tipo){
                case 1: 
                    return (red+green+blue)/3;
                    
                case 2: 
                    return (int)(.3*red)+(int)(.59*green)+(int)(.11*blue); 

                case 3:
                    return (int)(.2126*red)+(int)(.7152*green)+(int)(.0722*blue);

                case 4: 
                    return  (Math.max(Math.max(red, green), blue) + Math.min(Math.min(red, green), blue))/2; 

                case 5: 
                    return Math.max(Math.max(red, green), blue); 

                case 6:
                    return Math.min(Math.min(red, green), blue);

                case 7: 
                    return red; 

                case 8: 
                    return green; 

                case 9:
                    return blue; 

                default: 
                    return 0;     
            

            }
        }

        /**
         * Metodo para aplicar el filtro brillo a una Imagen dado la constante 
         * @param constante
         */
        public void filtroBrillo(int constante){

            //Verifica que la constante sea un numero 
    
            try {
                for(int i=0; i < ancho; i++){
                    for(int j=0; j< alto; j++){
                        //Obtiene el valor de cada pixel 
                        int pixel = imagenFiltrada.getRGB(i, j);
                        // Generamos el color que rellenara a cada pixel 
                        Color color = new Color(pixel,true);
                        //Obtenemos los colores de ese pixel 
                        int r = color.getRed(); 
                        int g = color.getGreen(); 
                        int b = color.getBlue();
    
                        int nr=0;
                        int ng=0; 
                        int nb=0; 
    
                        if(constante >= 0){
                            nr = (r+constante > 255)? 255:r+constante; 
                            ng = (g+constante > 255)? 255:g+constante; 
                            nb = (b+constante > 255)? 255:b+constante; 
                        }
                        if(constante < 0){
                            nr = (r+constante < 0)? 0:r+constante; 
                            ng = (g+constante < 0)? 0:g+constante; 
                            nb = (b+constante < 0)? 0:b+constante; 
                        }
                        
                        color = new Color(nr,ng,nb);
                        imagenFiltrada.setRGB(i, j, color.getRGB()); 
    
                    }
                }    
            } catch (Exception e) {
                //TODO: handle exception
            }
            
        }


        public BufferedImage getImagenFiltrada(){
            return imagenFiltrada; 
        }

        /**
         * Metodo para aplicar el filtro Mosaico a la imagen dadas las dimensiones del mosaico 
         * @param a
         * @param l
         */
        public void filtroMosaico(int a, int l){

            int totalIteraciones = 0; 

            for (int i = 0; i < ancho; i+=a) {
                for (int j = 0; j < alto; j+=l) { 
                    Color c = this.getColorPromedio(i, j, a+i, l+j);
                    aplicarColor(c, i, j, a+i, l+j);
                    if(j == alto-l){
                        aplicarColor(c, i, alto-l, a+i, alto);
                    }  
                    if (i == ancho-a && j < alto) {
                        aplicarColor(c, ancho-a, j, ancho, l+j);
                    }                     
                    
                }
                
            }
            
        }

        /**
         * Metodo para aplicar un color a una region dada 
         * @param c
         * @param rengI
         * @param colI
         * @param rengF
         * @param colF
         */
        public void aplicarColor(Color c, int rengI, int colI, int rengF, int colF){
            try {
                for (int renglon = rengI; renglon < rengF; renglon++) {
                    for (int columna = colI ; columna < colF; columna++) {
                        imagenFiltrada.setRGB(renglon, columna, c.getRGB());
                    }     
                }
            } catch (Exception e) {
                //TODO: handle exception
            }
        }

        /**
         * Metodo para obtener el color promedio dada una region
         * @param rengI
         * @param colI
         * @param rengF
         * @param colF
         * @return
         */
        public Color getColorPromedio(int rengI, int colI, int rengF, int colF){


            int totaRed = 0; 
            int totalGreen = 0;
            int totalBlue = 0; 

            int promRed=0; 
            int promGreen =0; 
            int promBlue =0; 
  
            //Se obtiene el total de pixeles para obtener el video 
            int pixelesTotales = (rengF - rengI)*(colF-colI);

            try {
                for (int renglon = rengI; renglon < rengF; renglon++) {
                    for (int columna = colI ; columna < colF; columna++) {
                        int pixel = imagenFiltrada.getRGB(renglon, columna);
                        // Generamos el color de que obtendremos informacion 
                        Color color = new Color(pixel,true);
    
                        totaRed += color.getRed();
                        totalGreen += color.getGreen(); 
                        totalBlue += color.getBlue(); 
                    }     
                }
                
            } catch (Exception e) {
                
            }
             
            promRed = totaRed/pixelesTotales; 
            promGreen = totalGreen/pixelesTotales; 
            promBlue = totalBlue/pixelesTotales; 

            

            Color promColor = new Color(promRed, promGreen, promBlue);
            return promColor; 

        }


} 