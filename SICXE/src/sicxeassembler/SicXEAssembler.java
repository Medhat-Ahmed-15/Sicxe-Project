
package sicxeassembler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import static java.lang.System.out;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SicXEAssembler {
    static HashMap<String, Instruction> opTab = new HashMap<>();
    static HashMap<String, Integer> symTab = new HashMap<>();
    static int locCtr = 0x00 ;
    static int prevLocCtr = 0x00;
    static int startingAddress = 0x00;
    static String programName = "      ";
    static ArrayList<String> list=new ArrayList<String>();
    static int programLength = 0;
    static HashMap<Integer, Integer> linesTab = new HashMap<>();
    static HashMap<String, Literal> litTab = new HashMap<>();
    
    
    static String[] words;
    static String[] col3;
    static String type;
    
    static int first;
    static int second;
    
   static File f;
   static File symbolFile;
   static File literalfile;
   static File mainOutput;
   
    
   
   static PrintWriter print_PASSS1ymbolFile;
   static PrintWriter print_PASS1LiteralFile;
   static PrintWriter print_PASS1MainOutput;
   
   
    public static void main(String[] args) throws FileNotFoundException {
        opTab.put("ADD", new Instruction(0x18, 3));
        opTab.put("ADDF", new Instruction(0x58, 3));
        opTab.put("ADDR", new Instruction(0x90, 2));
        opTab.put("AND", new Instruction(0x40, 3));
        opTab.put("CLEAR", new Instruction(0xB4, 2));
        opTab.put("COMP", new Instruction(0x28, 3));
        opTab.put("COMPF", new Instruction(0x88, 3));
        opTab.put("COMPR", new Instruction(0xA0, 2));
        opTab.put("DIV", new Instruction(0x24, 3));
        opTab.put("DIVF", new Instruction(0x64, 3));
        opTab.put("DIVR", new Instruction(0x9C, 2));
        opTab.put("FIX", new Instruction(0xC4, 1));
        opTab.put("FLOAT", new Instruction(0xC0, 1));
        opTab.put("HIO", new Instruction(0xF4, 1));
        opTab.put("J", new Instruction(0x3C, 3));
        opTab.put("JEQ", new Instruction(0x30, 3));
        opTab.put("JGT", new Instruction(0x34, 3));
        opTab.put("JLT", new Instruction(0x38, 3));
        opTab.put("JSUB", new Instruction(0x48, 3));
        opTab.put("LDA", new Instruction(0x00, 3));
        opTab.put("LDB", new Instruction(0x68, 3));
        opTab.put("LDCH", new Instruction(0X50, 3));
        opTab.put("LDF", new Instruction(0x70, 3));
        opTab.put("LDL", new Instruction(0x08, 3));
        opTab.put("LDS", new Instruction(0x6C, 3));
        opTab.put("LDT", new Instruction(0x74, 3));
        opTab.put("LDX", new Instruction(0x04, 3));
        opTab.put("LPS", new Instruction(0xD0, 3));
        opTab.put("MUL", new Instruction(0x20, 3));
        opTab.put("MULF", new Instruction(0x60, 3));
        opTab.put("MULR", new Instruction(0x98, 2));
        opTab.put("NORM", new Instruction(0xC8, 1));
        opTab.put("OR", new Instruction(0x44, 3));
        opTab.put("RD", new Instruction(0xD8, 3));
        opTab.put("RMO", new Instruction(0xAC, 2));
        opTab.put("RSUB", new Instruction(0x4C, 3));
        opTab.put("SHIFTL", new Instruction(0xA4, 2));
        opTab.put("SHIFTR", new Instruction(0xA8, 2));
        opTab.put("SIO", new Instruction(0xF0, 1));
        opTab.put("SSK", new Instruction(0xEC, 3));
        opTab.put("STA", new Instruction(0x0C, 3));
        opTab.put("STB", new Instruction(0x78, 3));
        opTab.put("STCH", new Instruction(0x54, 3));
        opTab.put("STF", new Instruction(0x80, 3));
        opTab.put("STI", new Instruction(0xD4, 3));
        opTab.put("STL", new Instruction(0x14, 3));
        opTab.put("STS", new Instruction(0x7C, 3));
        opTab.put("STSW", new Instruction(0xE8, 3));
        opTab.put("STT", new Instruction(0x84, 3));
        opTab.put("STX", new Instruction(0x10, 3));
        opTab.put("SUB", new Instruction(0x1C, 3));
        opTab.put("SUBF", new Instruction(0x5C, 3));
        opTab.put("SUBR", new Instruction(0x94, 2));
        opTab.put("SVC", new Instruction(0xB0, 2));
        opTab.put("TD", new Instruction(0xE0, 3));
        opTab.put("TIO", new Instruction(0xF8, 1));
        opTab.put("TIX", new Instruction(0x2C, 3));
        opTab.put("TIXR", new Instruction(0xB8, 2));
        opTab.put("WD", new Instruction(0xDC, 3));
        
         f = new File("code.txt");
        
         symbolFile = new File("symbol_table.txt");
         literalfile = new File("literals.txt");
         mainOutput = new File("MainOutput.txt");
        
        
        
        
        try {
        	
        	
            // Pass 1
         
            Scanner s = new Scanner(f);
            
            //Scanner scan_PASSS1ymbolFile = new Scanner(symbolFile);
             print_PASSS1ymbolFile = new PrintWriter(symbolFile);
            
            //Scanner scan_PASS1Literalfile = new Scanner(literalfile);
             print_PASS1LiteralFile = new PrintWriter(literalfile);
             print_PASS1MainOutput = new PrintWriter(mainOutput);
             
            
           
            
        	
            
            while (s.hasNext()) {
            	
                String line = s.nextLine();
               

                int lineIndex=0;
                System.out.println(String.format("%04X", locCtr) +": " + line);
                print_PASS1MainOutput.println(String.format("%04X", locCtr) +": " + line);
                linesTab.put(lineIndex,locCtr);
                 words = line.split(" ");
                 //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                if (words.length == 3) {
                	
                    if (words[1].equals("START")) {
                        programName = words[0];
                        startingAddress = Integer.parseInt(words[2], 16);
                        locCtr = startingAddress;
                        continue;//The continue keyword is used to end the current iteration in a for loop (or a while loop), and continues to the next iteration.
                    } else {
                        if (symTab.containsKey(words[0])) {
                            System.out.println("ERROR: Label already added in SymTab");
                            
                        } else {
                            symTab.put(words[0], locCtr);
                        		
                        	                    }
                      
                    }
                    
                   
                    
                    if (words[1].equals("RESW")) {
                        locCtr += (3 * Integer.parseInt(words[2]));
                    }
                    
                    else if (words[1].equals("RESDW"))
                    {
                  	  locCtr += (6 * Integer.parseInt(words[2]));
                  } 
                    
                    else if (words[1].equals("RESB")) {
                        locCtr += Integer.parseInt(words[2]);
                    }
                    
                    
                    
                    else if (words[1].equals("BYTE")) {
                    	
                    	//this condition 👇 if there is more than one string 👌 👌 👌
                        if (words[2].contains(",")) {
                            String array[] = words[2].split(",");
                            for (String element : array)
                            	
                                incrementByteLocCtr(element);
                            
                        } else {
                            incrementByteLocCtr(words[2]);
                        }
                    } else if (words[1].equals("WORD")) {
                        if (words[2].contains(",")) {
                            String array[] = words[2].split(",");
                            locCtr += (3 * array.length);
                        } else {
                            locCtr += 3;
                        }
                    } else if (words[1].equals("EQU")) {
                        try {
                            int value =  Integer.parseInt(words[2]);
                            
                            symTab.put(words[0], value);
                            
                        
                        		
                        	                            /* JavaObject Oriented ProgrammingProgramming. The NumberFormatException is 
                             * an unchecked exception thrown by parseXXX() methods when they are 
                             * unable to format (convert) a string into a number. The NumberFormatException can be thrown by many methods/
                             * constructors in the classes of java. lang package.*/
                        		
                        		
                        } catch (NumberFormatException e) {
                            if (symTab.containsKey(words[2])) {
                                int value = symTab.get(words[2]);
                                symTab.put(words[0], value);
                               
                            	                         }
                        }
                    } else if (opTab.containsKey(words[1].replace("+", ""))) {
                        Instruction i = opTab.get(words[1].replace("+", ""));
                        if (i.format == 1)
                            locCtr += 1;
                        else if (i.format == 2)
                            locCtr += 2;
                        else if (i.format == 3) {
                            if (words[1].contains("+"))
                                locCtr += 4;
                             
                            else
                                locCtr += 3;
                            
                        }
                        if (words[2].contains("=")) {
                            addLiteral(words[2]);
                        }
                    } 
                    
                    
                    else if (opTab.containsKey(words[1].replace("$", ""))) {
                        Instruction i = opTab.get(words[1].replace("$", ""));
                        if (i.format == 1)
                            locCtr += 1;
                        else if (i.format == 2)
                            locCtr += 2;
                        else if (i.format == 3) {
                            if (words[1].contains("$"))
                                locCtr += 3;
                             
                            else
                                locCtr += 3;
                            
                        }
                        if (words[2].contains("=")) {
                            addLiteral(words[2]);
                        }
                    } 
                    
                    
                    
                    
                    else if (words[0].contains(".")) {
                    	
                    	System.out.println("this is a comment");
                    	print_PASS1MainOutput.println("this is a comment");
                    
                    } 
                    
                    
                    
                  
                    else {
                        System.out.println("ERROR: Invalid instruction");
                      return;
                    }
                    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                } else if (words.length == 2) {
                    if (words[0].equals("END")) {
                        for (String name : litTab.keySet()) {
                            Literal l = litTab.get(name);
                            if (l.address == 0) {
                                l.address = locCtr;
                                locCtr += l.length;
                            }
                        }
                    } else if (words[0].equals("ORG")) {
                        try {
                            int value = Integer.parseInt(words[1], 16);
                            prevLocCtr = locCtr;
                            locCtr = value;
                        } catch (NumberFormatException e) {
                            if (symTab.containsKey(words[1])) {
                                int value = symTab.get(words[1]);
                                prevLocCtr = locCtr;
                                locCtr = value;
                            } else {
                                System.out.println("ERROR: Invalid label");
                                return;
                            }
                        }
                    } else if (opTab.containsKey(words[0].replace("+", ""))) {
                        Instruction i = opTab.get(words[0].replace("+", ""));
                        if (i.format == 1)
                            locCtr += 1;
                        else if (i.format == 2)
                            locCtr += 2;
                        else if (i.format == 3) {
                            if (words[0].contains("+"))
                                locCtr += 4;
                            else
                                locCtr += 3;
                        }
                        
                        if (words[1].contains("="))
                            addLiteral(words[1]);
                    }
                    
                    
                    else if (opTab.containsKey(words[0].replace("$", ""))) {
                        Instruction i = opTab.get(words[0].replace("$", ""));
                        if (i.format == 1)
                            locCtr += 1;
                        else if (i.format == 2)
                            locCtr += 2;
                        else if (i.format == 3) {
                            if (words[0].contains("$"))
                                locCtr += 3;
                            else
                                locCtr += 3;
                        }
                        
                        if (words[1].contains("="))
                            addLiteral(words[1]);
                    }
                    
                    
                    
                    else if (opTab.containsKey(words[1].replace("+", ""))) {
                        if (symTab.containsKey(words[0])) {
                            System.out.println("ERROR: Label already added in SymTab");
                            return;
                        } else {
                            symTab.put(words[0], locCtr);
                          
                        	
                        		
                        	  Instruction i = opTab.get(words[1].replace("+", ""));
                            if (i.format == 1)
                                locCtr += 1;
                            else if (i.format == 2)
                                locCtr += 2;
                            else if (i.format == 3) {
                                if (words[1].contains("+"))
                                    locCtr += 4;
                                else
                                    locCtr += 3;
                            }
                        }
                    } 
                    
                    
                    else if (opTab.containsKey(words[1].replace("$", ""))) {
                        if (symTab.containsKey(words[0])) {
                            System.out.println("ERROR: Label already added in SymTab");
                            return;
                        } else {
                            symTab.put(words[0], locCtr);
                           
                                          
                        		Instruction i = opTab.get(words[1].replace("$", ""));
                        		
                            if (i.format == 1)
                                locCtr += 1;
                            else if (i.format == 2)
                                locCtr += 2;
                            else if (i.format == 3) {
                                if (words[1].contains("$"))
                                    locCtr += 3;
                                else
                                    locCtr += 3;
                            }
                        }
                    } 
                    
                    else if (words[0].equals("BASE")) {
                    	
                    	 System.out.println("No Location counter");
                        
                    } 
                    
                         else if (words[0].contains(".")) {
                    	
                    	System.out.println("This is a comment");
                    	print_PASS1MainOutput.println("this is a comment");
                    
                    } 
                    
                    else {
                        System.out.println("ERROR: Invalid instruction");
                        return;
                    }
                    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                } else if (words.length == 1) {
                    if (words[0].equals("END")) {
                        for (String name : litTab.keySet()) {
                            Literal l = litTab.get(name);
                            if (l.address == 0) {
                                l.address = locCtr;
                                locCtr += l.length;
                            }
                        }
                    } else if (words[0].equals("ORG")) {
                        if (prevLocCtr == 0) {
                            System.out.println("ERROR: No previous org found");
                            return;
                        }
                        locCtr = prevLocCtr;
                        
                   } else if (words[0].equals("LTORG")) {
                        for (String name : litTab.keySet()) {
                            Literal l = litTab.get(name);
                            if (l.address == 0) {
                                l.address = locCtr;
                                locCtr += l.length;
                            }
                        }
                    } else if (opTab.containsKey(words[0].replace("+", ""))) {
                        Instruction i = opTab.get(words[0].replace("+", ""));
                        if (i.format == 1)
                            locCtr += 1;
                        else if (i.format == 2)
                            locCtr += 2;
                        else if (i.format == 3) {
                            if (words[0].contains("+"))
                                locCtr += 4;
                            else
                                locCtr += 3;
                        }
                    }
                    
                    
                    else if (opTab.containsKey(words[0].replace("$", ""))) {
                        Instruction i = opTab.get(words[0].replace("$", ""));
                        if (i.format == 1)
                            locCtr += 1;
                        else if (i.format == 2)
                            locCtr += 2;
                        else if (i.format == 3) {
                            if (words[0].contains("$"))
                                locCtr += 3;
                            else
                                locCtr += 3;
                        }
                    }
                    
                          else if (words[0].contains(".")) {
                    	
                    	System.out.println("This is a comment");
                    	print_PASS1MainOutput.println("this is a comment");
                    
                    } 
                    
                    
                    
                   else {
                        System.out.println("ERROR: Invalid instruction");
                        return;
                    }
                } else {

                }
            }
            
            for (Object key : symTab.keySet()) {
                System.out.print(key + " = " + String.format("%06X", symTab.get(key)) + ", ");
            }
            System.out.println();
            for (Object key : litTab.keySet()) {
                System.out.print(key + " = " + litTab.get(key) + ", ");
                print_PASS1LiteralFile.println(key + " = " + litTab.get(key) + ", ");
            }
            System.out.println();

            print_PASSS1ymbolFile.close();
            print_PASS1LiteralFile.close();
            print_PASS1MainOutput.close();
            
            
            //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            
            //PASS 2..................................
            
            File outputFile = new File("output.txt");
            File hteFile = new File("HTE_Record.txt");
            
            PrintWriter out = new PrintWriter(outputFile);
            PrintWriter print_hterecord = new PrintWriter(hteFile);
            
             locCtr=startingAddress;
             int currentLocCtr = locCtr;
             String objCode="";
             int baseloc = 0;
              Scanner s2 = new Scanner(f);  
              String tRecord = "";
              int tStartingAddress = 0;
               while (s2.hasNext()) {
                   objCode = "";
                    String line = s2.nextLine();
                    String words[] = line.split(" ");
                    int opCode =0;
                    currentLocCtr = locCtr;
                   if (words.length == 3) {
                        if (words[1].equals("START")) {
                            String programName = words[0];
                            if (programName.length() > 6) {
                                programName = programName.substring(0, 6);
                            } else if (programName.length() < 6) {
                                while (programName.length() < 6)
                                    programName += " ";
                            }
                            System.out.println("H " + programName + " " + String.format("%06X", startingAddress) + " " + String.format("%06X", programLength));
                            
                            print_hterecord.println("H " + programName + " " + String.format("%06X", startingAddress) + " " + String.format("%06X", programLength));
                            
                            tStartingAddress = startingAddress;
                            
            }               
                        else {
                            if(opTab.containsKey(words[1]))
                            {
                                Instruction ins = opTab.get(words[1]);
                                if (ins.format == 1)
                                {
                                    objCode=String.format("%02x",ins.opCode);
                                    
                                    locCtr += 1;
                                   
                                }
                                else if (ins.format == 2)
                                { 
                                    locCtr += 2;
                                     String reg[]=words[2].split(","); 
                                     int r1=0,r2=0;
                                     if(reg[0].equals("A"))
                                          r1=0;
                                      else if(reg[0].equals("X"))
                                          r1=1;
                                      else if(reg[0].equals("L"))
                                          r1=2;
                                      else if(reg[0].equals("B"))
                                          r1=3;
                                      else if(reg[0].equals("S"))
                                          r1=4;
                                      else if(reg[0].equals("T"))
                                          r1=5;
                                      else if(reg[0].equals("F"))
                                          r1=6;
                                      if (reg.length > 1) {
                                      if(reg[1].equals("A"))
                                          r2=0;
                                      else if(reg[1].equals("X"))
                                          r2=1;
                                      else if(reg[1].equals("L"))
                                          r2=2;
                                      else if(reg[1].equals("B"))
                                          r2=3;
                                      else if(reg[1].equals("S"))
                                          r2=4;
                                      else if(reg[1].equals("T"))
                                          r2=5;
                                      else if(reg[1].equals("F"))
                                          r2=6;
                                      }
                                    objCode=String.format("%02X",ins.opCode)+String.format("%01X", r1)+String.format("%01X",r2); 
                                    
                                }else if (ins.format == 3) {
                                    
                                   locCtr += 3;
                                   int loc;
                                   int diff;
                                   String binary= tobin(ins.opCode, 6 );
                                   
                                   int n=1,i=1,x=0,b=0,p=1,e=0;
                                   
                                    if (words[2].contains(",X")) {
                                         x=1;
                                     }
                                   if(words[2].contains("="))
                                      {
                                        Literal l=litTab.get(words[2].replace("=", ""));
                                        loc = l.address; 
                                        diff = l.address - locCtr;
                                        objCode = String.format("%03X", Integer.parseUnsignedInt(binary+n+i+x+b+p+e, 2)) + String.format("%03X", diff);
                                      }
                                   //.................................................................
                                   
                                   else if(words[2].contains("#")) 
                                    {
                                        n=0;i=1;
                                      String imm=words[2].replace("#","");
                                     try{

                                         int im = Integer.parseInt(imm);
                                         b = 0; p = 0;
                                         diff = 0;
                                         loc = 0;
                                        objCode = String.format("%03X", Integer.parseUnsignedInt(binary+n+i+x+b+p+e, 2)) + String.format("%03X", im);


                                     } catch(NumberFormatException ex) {
                                         loc = symTab.get(words[2]);
                                         diff = loc - locCtr;
                                         b=0;p=1;
                                         objCode=String.format("%03X",Integer.parseUnsignedInt(binary+n+i+x+b+p+e, 2))+String.format("%03X",diff);

                                     }

                                    } 
                                      else if(words[2].contains("@")){
                                         n=1;i=0;
                                          String ind=words[2].replace("@","");
                                          loc = symTab.get(ind);
                                          diff = loc - locCtr;
                                         objCode=String.format("%03X",Integer.parseUnsignedInt(binary+n+i+x+b+p+e, 2))+String.format("%03X",diff);    
                                      }
                                      
                                    else{
                                         n=i=1; 
                                       loc = symTab.get(words[2]);
                                       diff=loc-locCtr;
                                       if (diff < 0) {
                                           String bin = Integer.toBinaryString(diff);
                                           bin = bin.substring(bin.length() - 12, bin.length());
                                           diff = Integer.parseInt(bin, 2);
                                       }
                                      objCode=String.format("%03X", Integer.parseUnsignedInt(binary+n+i+x+b+p+e, 2))+String.format("%03X", diff);
                                   }
                                   if(-2048<=diff&& diff<=2047)
                                   {
                                       
                                      b=0;p=1;
                                     
                                   } else if (loc - baseloc >= 0 && loc - baseloc <= 4095) {
                                       b=1;p=0;
                                        diff = loc - baseloc;
                                        objCode=String.format("%03X", Integer.parseUnsignedInt(binary+n+i+x+b+p+e, 2))+String.format("%03X", diff);
                                   }
                                   else
                                   {
                                   out.println("error");
                                   }
                                  
                                }
                            } else if (opTab.containsKey(words[1].replace("+", "")))
                            {
                                
                              list.add((locCtr+1)+"");  
                              locCtr+=4;
                              int n=1,i=1,x=0,b=0,p=0,e=1;
                              Instruction ins = opTab.get(words[1].replace("+", ""));
                             String binary=tobin(ins.opCode,6);
                             int loc;
                              
                             if(words[2].contains("="))
                                      {
                                        Literal l=litTab.get(words[2].replace("=", ""));
                                        loc = l.address; 
                                        
                                   }
                            
                             else if(words[2].contains("#")) 
                                    {
                                        n=0;i=1;
                                      String imm=words[2].replace("#","");
                                     try{

                                         int im = Integer.parseInt(imm);
                                        objCode = String.format("%03X", Integer.parseUnsignedInt(binary+n+i+x+b+p+e, 2)) + String.format("%05X", im);


                                     } catch(NumberFormatException exx) {
                                         loc=symTab.get(imm);
                                         objCode=String.format("%03X",Integer.parseUnsignedInt(binary+n+i+x+b+p+e, 2))+String.format("%05X",loc);

                                     }

                                    } 
                                      else if(words[2].contains("@")){
                                         n=1;i=0;
                                          String ind=words[2].replace("@","");
                                          loc = symTab.get(ind);
                                         objCode=String.format("%03X",Integer.parseUnsignedInt(binary+n+i+x+b+p+e, 2))+String.format("%05X",loc);    
                                      
                        }    else{
                                          loc = symTab.get(words[2]);
                                      objCode=String.format("%03X", Integer.parseUnsignedInt(binary+n+i+x+b+p+e, 2))+String.format("%05X", loc);
                                   }
                             
                             
                             
                        }
                            //$Format///////////////////////////////////////
                            
                            else if (opTab.containsKey(words[1].replace("$", "")))
                            {
                                
                              
                              locCtr+=3;
                              int n=1,i=1,x=0,b=0,p=0,e=1;
                              Instruction ins = opTab.get(words[1].replace("$", ""));
                              
                              int loc;
                              int diff;
                              loc = symTab.get(words[2]);
                              diff = loc - locCtr;
                              
                              String binary= tobin(ins.opCode, 6 );
                              
                              if (words[2].contains(",X")) {
                                  x=1;
                              }
                              
                              
                            if(words[2].contains("="))
                               {
                                 Literal l=litTab.get(words[2].replace("=", ""));
                                 loc = l.address; 
                                 diff = l.address - locCtr;
                                 objCode = String.format("%03X", Integer.parseUnsignedInt(binary+n+i+x+b+p+e, 2)) + String.format("%03X", diff);
                               }
                            
                            
                             else if(diff%2==0&&diff>0) 
                                    {
                                        n=1;i=1;
                                        objCode = String.format("%03X", Integer.parseUnsignedInt(binary+n+i+x+b+p+e, 2)) + String.format("%03X", diff);

                                    } 
                                      else if(diff>=0){
                                         n=0;i=1;
                                         objCode = String.format("%03X", Integer.parseUnsignedInt(binary+n+i+x+b+p+e, 2)) + String.format("%03X", diff);
                                         
                                       
                       }   
                                      else {
                                     	 n=1;i=0;
                                     	 objCode = String.format("%03X", Integer.parseUnsignedInt(binary+n+i+x+b+p+e, 2)) + String.format("%03X", diff);
                                      }
                             
                        }
                            
                            //$Format///////////////////////////////////////
                            
                            
                            
                            
                             else  if (words[1].equals("RESW")) {
                            locCtr += (3 * Integer.parseInt(words[2]));
                            if (!tRecord.isEmpty()) {
                                int length = tRecord.replace(" ", "").length();
                                length /= 2;
                                System.out.println("T " + String.format("%06X", tStartingAddress) + " " + String.format("%02X", length) + " " + tRecord);
                                print_hterecord.println("T " + String.format("%06X", tStartingAddress) + " " + String.format("%02X", length) + " " + tRecord);
                                
                            }
                            tRecord = "";
                            tStartingAddress = locCtr;
                        } 
                        else if (words[1].equals("RESB")) {
                            locCtr += Integer.parseInt(words[2]);
                            if (!tRecord.isEmpty()) {
                                int length = tRecord.replace(" ", "").length();
                                length /= 2;
                                System.out.println("T " + String.format("%06X", tStartingAddress) + " " + String.format("%02X", length) + " " + tRecord);
                                print_hterecord.println("T " + String.format("%06X", tStartingAddress) + " " + String.format("%02X", length) + " " + tRecord);
                            }
                            tRecord = "";
                            tStartingAddress = locCtr;
                        } else if (words[1].equals("BYTE")) {
                            incrementByteLocCtr(words[2]);
                            if (words[2].contains("X")) {
                                    words[2] = words[2].substring(2, words[2].length()-1);
                                    objCode = String.format("%02X", Integer.parseInt(words[2], 16));
                                } else if (words[2].contains("C")) {
                                    words[2] = words[2].substring(2, words[2].length()-1);
                                    for (char c : words[2].toCharArray())
                                        objCode += String.format("%02X", (int) c);
                                } else {
                                    objCode = String.format("%02X", Integer.parseInt(words[2]));
                            }
                            
                        } else if (words[1].equals("WORD")) {
                            locCtr += 3;
                            objCode = String.format("%06X", Integer.parseInt(words[2]));
                        } else if (words[1].equals("EQU")) {
                            try {
                                int value =  Integer.parseInt(words[2]);
                                symTab.put(words[0], value);
                            } catch (NumberFormatException ex) {
                                if (symTab.containsKey(words[2])) {
                                    int value = symTab.get(words[2]);
                                    symTab.put(words[0], value);
                                }
                            }
                        }
                        
                                    
                         
                         
                        }}if (words.length == 2) {
                            if (words[0].equals("BASE")) {
                                int address = 0;
                                if (symTab.containsKey(words[1])) address = symTab.get(words[1]);
                                baseloc = address;
                            }else if(words[0].contains("END"))
                            {
                                 for (String name : litTab.keySet()) {
                                Literal l = litTab.get(name);
                                if (l.address == locCtr) {
                                    locCtr += l.length;
                                    objCode = l.value;
                                }
                            }
                            }
                            else if(opTab.containsKey(words[0]))
                            {
                                Instruction ins = opTab.get(words[0]);
                                if (ins.format == 1)
                                {
                                    objCode=String.format("%02X",ins.opCode);
                                    locCtr += 1;
                                }
                                else if (ins.format == 2)
                                { 
                                    locCtr += 2;
                                     String reg[]=words[1].split(","); 
                                     int r1=0,r2=0;
                                     if(reg[0].equals("A"))
                                          r1=0;
                                      else if(reg[0].equals("X"))
                                          r1=1;
                                      else if(reg[0].equals("L"))
                                          r1=2;
                                      else if(reg[0].equals("B"))
                                          r1=3;
                                      else if(reg[0].equals("S"))
                                          r1=4;
                                      else if(reg[0].equals("T"))
                                          r1=5;
                                      else if(reg[0].equals("F"))
                                          r1=6;
                                      if (reg.length > 1) {
                                      if(reg[1].equals("A"))
                                          r2=0;
                                      else if(reg[1].equals("X"))
                                          r2=1;
                                      else if(reg[1].equals("L"))
                                          r2=2;
                                      else if(reg[1].equals("B"))
                                          r2=3;
                                      else if(reg[1].equals("S"))
                                          r2=4;
                                      else if(reg[1].equals("T"))
                                          r2=5;
                                      else if(reg[1].equals("F"))
                                          r2=6;
                                      }
                                    objCode=String.format("%02X",ins.opCode)+String.format("%01X", r1)+String.format("%01X",r2); 
                                }
                                
                                else if (ins.format == 3) {
                                    
                                   locCtr += 3;
                                   int loc;
                                   int diff;
                                      String binary= tobin(ins.opCode, 6 );
                                   int n=1,i=1,x=0,b=0,p=1,e=0;
                                   
                                    if (words[1].contains(",X")) {
                                        x=1;
                                        words[1] = words[1].replace(",X", "");
                                     }
                                   if(words[1].contains("="))
                                      {
                                        Literal l=litTab.get(words[1].replace("=", ""));
                                        loc = l.address; 
                                        diff = l.address - locCtr;
                                        objCode=String.format("%03X", Integer.parseUnsignedInt(binary+n+i+x+b+p+e, 2))+String.format("%03X", diff);

                                      }
                                
                                   
                                   else if(words[1].contains("#")) 
                                    {
                                      n=0;i=1;
                                      String imm=words[1].replace("#","");
                                     try{

                                         int im = Integer.parseInt(imm);
                                         b = 0; p = 0;
                                         diff = 0;
                                         loc = 0;
                                         objCode = String.format("%03X", Integer.parseUnsignedInt(binary+n+i+x+b+p+e, 2)) + String.format("%03X", im);


                                     } catch(NumberFormatException ex) {
                                         loc = symTab.get(imm);
                                         diff = loc - locCtr;
                                         b=0;p=1;
                                         objCode=String.format("%03X",Integer.parseUnsignedInt(binary+n+i+x+b+p+e, 2))+String.format("%03X",diff);

                                     }

                                    } 
                                      else if(words[1].contains("@")){
                                         n=1;i=0;
                                          String ind=words[1].replace("@","");
                                          loc = symTab.get(ind);
                                          diff = loc - locCtr;
                                         objCode=String.format("%03X",Integer.parseUnsignedInt(binary+n+i+x+b+p+e, 2))+String.format("%03X",diff);    
                                      }
                                      
                                    else{
                                       n=i=1; 
                                       loc = symTab.get(words[1]);
                                       diff=loc-locCtr;
                                       if (diff < 0) {
                                           String bin = Integer.toBinaryString(diff);
                                           bin = bin.substring(bin.length() - 12, bin.length());
                                           diff = Integer.parseInt(bin, 2);
                                       }
                                       objCode=String.format("%03X", Integer.parseUnsignedInt(binary+n+i+x+b+p+e, 2))+String.format("%03X", diff);
                                   }
                                
                                   
                                   if(-2048<=diff&& diff<=2047)
                                   {
                                      b=0;p=1;
                                     
                                   } else if (loc - baseloc >= 0 && loc - baseloc <= 4095) {
                                        b=1;p=0;
                                        diff = loc - baseloc;
                                        objCode=String.format("%03X", Integer.parseUnsignedInt(binary+n+i+x+b+p+e, 2))+String.format("%03X", diff);
                                   }
                            
                                }
                            }
                            else if (opTab.containsKey(words[0].replace("+", "")))
                            {
                                 list.add((locCtr+1)+"");
                                 locCtr+=4;
                                 int n=1,i=1,x=0,b=0,p=0,e=1;
                                 Instruction ins = opTab.get(words[0].replace("+", ""));
                                 String binary=tobin(ins.opCode,6);
                                 int loc;
                              
                             if(words[1].contains("="))
                                      {
                                        Literal l=litTab.get(words[1].replace("=", ""));
                                        loc = l.address; 
                                   }
                            
                             else if(words[1].contains("#")) 
                                    {
                                        n=0;i=1;
                                      String imm=words[1].replace("#","");
                                     try{

                                         int im = Integer.parseInt(imm);
                                        objCode = String.format("%03X", Integer.parseUnsignedInt(binary+n+i+x+b+p+e, 2)) + String.format("%05X", im);


                                     } catch(NumberFormatException ex) {
                                         loc=symTab.get(imm);
                                         objCode=String.format("%03X",Integer.parseUnsignedInt(binary+n+i+x+b+p+e, 2))+String.format("%05X",loc);

                                     }

                                    }
                                      else if(words[1].contains("@")){
                                         n=1;i=0;
                                          String ind=words[1].replace("@","");
                                          loc = symTab.get(ind);
                                         objCode=String.format("%03X",Integer.parseUnsignedInt(binary+n+i+x+b+p+e, 2))+String.format("%05X",loc);    
                                      }
                                      
                                    else{
                                          loc = symTab.get(words[1]);
                                      objCode=String.format("%03X", Integer.parseUnsignedInt(binary+n+i+x+b+p+e, 2))+String.format("%05X", loc);
                                   }
                            }
                        
                        
                    }else if (words.length == 1) {
                            if (words[0].contains("LTORG"))
                            {
                               for (String name : litTab.keySet()) {
                                Literal l = litTab.get(name);
                                if (l.address == locCtr) {
                                    locCtr += l.length;
                                    objCode = l.value;
                                }
                            }
                            }
                            else if(words[0].contains("ORG"))
                            {
                                 try {
                                int value = Integer.parseInt(words[1], 16);
                                prevLocCtr = locCtr;
                                locCtr = value;
                            } catch (NumberFormatException e) {
                                if (symTab.containsKey(words[1])) {
                                    int value = symTab.get(words[1]);
                                    prevLocCtr = locCtr;
                                    locCtr = value;
                                } else {
                                    System.out.println("ERROR: Invalid label");
                                    return;
                                }
                            }
                            }
                            else if(words[0].contains("END"))
                            {
                                 for (String name : litTab.keySet()) {
                                Literal l = litTab.get(name);
                                if (l.address == locCtr) {
                                    locCtr += l.length;
                                    objCode = l.value;
                                }
                            }
                                
                            }
                            else if(opTab.containsKey(words[0]))
                            {
                                Instruction ins = opTab.get(words[0]);
                                if (ins.format == 1)
                                {
                                    objCode=String.format("%02X",ins.opCode);
                                    locCtr += 1;
                                   
                                
                                }
                                else if(ins.format==3)
                                {
                                   locCtr+=3;
                                   String binary= tobin(ins.opCode, 6 );
                                   int n=1,i=1,x=0,b=0,p=0,e=0;
                                   objCode=String.format("%03X", Integer.parseUnsignedInt(binary+n+i+x+b+p+e, 2))+String.format("%03X", 0);
                                   
                                } else if(words[0].contains("+"))
                                {
                                   String w=words[0].replace("+","");
                                   Instruction in = opTab.get(w);
                                   list.add((locCtr+1)+"");
                                   locCtr+=4;
                                   String binary= tobin(in.opCode, 6 );
                                   int n=1,i=1,x=0,b=0,p=0,e=0;
                                   objCode=String.format("%03X", Integer.parseUnsignedInt(binary+n+i+x+b+p+e, 2))+String.format("%05X", 0);
                                   
                                }
                            }
                   }
                    if (objCode.length() > 8) objCode = objCode.substring(0, 6);
                    if ((tRecord + " " + objCode).replace(" ", "").length() > 60) {
                        int length = tRecord.replace(" ", "").length();
                        length /= 2;
                        System.out.println("T " + String.format("%06X", tStartingAddress) + " " + String.format("%02X", length) + " " + tRecord);
                        print_hterecord.println("T " + String.format("%06X", tStartingAddress) + " " + String.format("%02X", length) + " " + tRecord);
                        tRecord = objCode;
                        tStartingAddress = currentLocCtr;
                        
                        
                    } else {
                    	
                        if (!objCode.isEmpty()) 
                        	
                        	{tRecord += " " + objCode;}
                    }
                        
                   out.println(line + " - " + objCode);
               }
                int length = tRecord.replace(" ", "").length();
               length /= 2;
               
               
               if (!tRecord.isEmpty()) {
            	   System.out.println("T " + String.format("%06X", tStartingAddress) + " " + String.format("%02X", length) + " " + tRecord);
            	   
                   print_hterecord.println("T " + String.format("%06X", tStartingAddress) + " " + String.format("%02X", length) + " " + tRecord);}
             
               for(int a=0;a<list.size();a++)
             {
                 String m=(""+list.get(a));
                 System.out.println("M " + String.format("%06X",(Integer.parseInt(m)))+" "+String.format("%02X",05));
                 print_hterecord.println("M " + String.format("%06X",(Integer.parseInt(m)))+" "+String.format("%02X",05));
             }
            
               System.out.println("E " + String.format("%06X", startingAddress));
               print_hterecord.println("E " + String.format("%06X", startingAddress));
               out.close();
               print_hterecord.close();
            
               //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
               ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
               ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
               
               //PASS 2..................................
            
        }catch (FileNotFoundException ex) {
            Logger.getLogger(SicXEAssembler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
                               
    static void incrementByteLocCtr(String word) {
    	
        if (word.contains("X")) {
            word = word.substring(2, word.length()-1);
            double bytes = word.length() / 2.0;
            bytes = Math.ceil(bytes);
            locCtr += bytes;
        } else if (word.contains("C")) {
            word = word.substring(2, word.length()-1);
            locCtr += word.length();
        } else {
            locCtr += 1;
        }
    }

    
    
    static void addLiteral(String word) {
        word = word.replace("=", "");
        if (litTab.containsKey(word))
        	 System.out.println("ERROR: Label already added in litTab");
        
        String name = word;
        String value = "";
        int length;
        if (word.contains("X")) {
            word = word.substring(2, word.length()-1);
            value = word;
            length = (int) Math.ceil(value.length() / 2);
        } 
        
        else if (word.contains("C")) {
            word = word.substring(2, word.length()-1);
            for (char c : word.toCharArray())
                value += String.format("%02X", (int) c);//(int c) dee batgeeb el askii code
            length = word.length();
        } else {
            value = String.format("%X", Integer.parseInt(word));
            length = value.length();
        }
        Literal l = new Literal(value, length);
        litTab.put(name, l);
       
        
    }
    
   /*The java.lang.Integer.toBinaryString() method returns a string representation of the integer 
    * argument as an unsigned integer in base 2. It accepts an argument in Int 
    * data-type and returns the corresponding binary string.*/
    
    /*
     Input : 10 
     Output : 1010 

     Input : 9
     Output : 1001 */
    
    
    public static String tobin ( int op, int size ){
        String bin= Integer.toBinaryString(op);
        if (size == 6) {
            while(bin.length() < 8) bin = "0" + bin;
            bin = bin.substring(0, 6);
        }
        else while (bin.length() < size) bin = "0" + bin;
      return bin;
      }
    
  
}



class Instruction {
    public int opCode;
    public int format;

    public Instruction(int opCode, int format) {
        this.opCode = opCode;
        this.format = format;
    }
}


class Literal {
    public String value;
    public int length;
    public int address = 0x00;

    public Literal(String value, int length) {
        this.value = value;
        this.length = length;
    }
    
    @Override
    public String toString() {
        return value + " - " + length + " - " + String.format("%04X", address);
    }
    
  
}

