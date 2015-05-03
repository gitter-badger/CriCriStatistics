import java.lang.*;
import java.io.File;
import java.io.IOException;
import org.abego.treelayout.util.DefaultTreeForTreeLayout;

/**
 * Noeud d'un arbre de fichiers, contient des données pour un affichage éventuel et permet un affichage sur la console
 * @author J. Pallamidessi & S. Andreux
 */
public class FileNode extends File
{
	int nbDir = 0;
	FileNode daddy = null;
	FileNode[] files = null;
	protected int x,y;
	protected int edgeSize;

	/**
	 * Crée un noeud dans l'arbre des fichiers
	 * @param dad 
	 * 		Référence le noeud parent
	 * @param son 
	 *  	Nom du fichier attaché au noeud
	 */
	public FileNode(FileNode dad, String son)
	{
		super(dad.getAbsolutePath(),son);
		daddy = dad;
	}

	/**
	 * Crée un noeud dans l'arbre des fichiers, le noeud créé correspond à la tête de l'arbre.
	 * @param son 
	 *  	Nom du fichier attaché au noeud
	 */
	public FileNode(String n)
	{
		super(n);			
	}

	/**
	 * Crée l'arbre
	 * @param depth
	 * 		Donne la profondeur souhaitée pour l'arbre
	 */	
	public void buildTree(TextInBox root, DefaultTreeForTreeLayout<TextInBox> tree)
	{
    int i, j, k = 0, sizeTab;
		FileNode cur;
		String[] f = list();
		
		if(isFile())
		{
			return ;
		}
		else 
		{
			if(f != null)
			{
				sizeTab = f.length;
				files = new FileNode[sizeTab];

				j = sizeTab - 1;
        
        TextInBox tb = new TextInBox(getName(), 20, 20);
        tree.addChild(root, tb);
				for(i = 0; i < sizeTab; i++)
				{
					cur = new FileNode(this, f[i]);
					if(cur.isDirectory())
					{
						files[k] = cur;
						cur.buildTree(tb, tree);
						nbDir++;
						k++;
					}
					else
					{
						files[j] = cur;
						j--;
					}
				}
			}
		}
		return ;
	}

	/**
	 * Renvoit un lien vers le noeud d'un fils du répertoire courant
	 * @param index 
	 *  	Numéro du fils qui doit être renvoyé
	 * @return 
	 * 		FileNode associé au fils
	 */
	public FileNode getSon(int index)
	{
		return files[index];
	}

	/**
	 * Indique si un répertoire est vide
	 * @return 
	 * 		booléen indiquant si le répertoire est vide
	 */
	public boolean isEmpty()
	{
		return (files == null);
	}

	/**
	 * Indique si un répertoire est vide au sens où la méthode list() ne revoit rien
	 * @return 
	 * 		booléen indiquant si la liste renvoyée par list est null
	 */
	public boolean isReallyEmpty()
	{
		return (list() == null);
	}


	/**
	 * Indique le nombre de fichiers (répertoires et sous-répertoires) contenus dans un répertoire
	 * @return 
	 * 		entier donnant le nombre de fichiers et sous-répertoires contenus dans le répertoire
	 */
	public int nbFiles()
	{
		return files.length;
	}


	/**
	 * Indique le nombre de fichiers contenus dans un répertoire
	 * @return 
	 * 		entier donnant le nombre de fichiers contenus dans le répertoire
	 */
	public int getNbFile()
	{
		int result = 0, i;

		for(i = 0; i < files.length; i++)
		{
			if(files[i].isFile())
				result++;
		}
		return result;
	}


	/**
	 * Indique le nombre de sous-répertoires contenus dans un répertoire
	 * @return 
	 * 		entier donnant le nombre de sous-répertoires contenus dans le répertoire
	 */
	public int getNbDirectory()
	{
		int result = 0, i;

		for(i = 0; i < files.length;i++)
		{
			if(files[i].isDirectory())
				result++;
		}
		return result;
	}


	/**
	 * Affiche les chemins des différents fichiers inclus dans le répertoire
	 */
	public void succ()
	{
		if(files != null)
		{
			int i;
			for(i = 0; i < files.length; i++)
			{
				System.out.println(files[i].getAbsolutePath());
			}
		}
	}


	/**
	 * Affiche ou en tout cas essaie d'affiche l'arbre des chemins des fichiers sur la console
	 */
	public void print()
	{
		int size,i,j;
		int nb_element=0;

		if(!exists()) 
			return ;
		
		if(isFile())
		{ 
			System.out.println(getName());
			System.out.println("x "+x+"y "+y);
		}
		else																// Cas dossier
		{
			System.out.println(getName()+"/");
			size=getName().length();
			System.out.println("x "+x+"y "+y);
			System.out.println(files.length);
			
			if(files==null) 									// Cas dossier vide
				return;

			for(i=0;i<files.length;i++)
			{
			
				for(j=0;j<size+3;j++)
					System.out.print(" ");
				
				if(files[i]!=null && files[i].isDirectory())
					files[i].print();
				
				
				if(files[i]!=null && files[i].isFile()){
					System.out.println(files[i].getName());
					System.out.println("x "+files[i].getX()+"y "+files[i].getY());
				}
			}
		}
	}


	/**
	 * Accesseur: retourne la valeur de x pour l'affichage graphique
	 * @return 
	 *		entier valeur de x
	 */
	public int getX()
	{
		return x;
	}


	/**
	 * Accesseur: retourne la valeur de y pour l'affichage graphique
	 * @return 
	 *		entier valeur de y
	 */
	public int getY()
	{
		return y;
	}


	/**
	 * Accesseur: retourne la taille du coté pour l'affichage
	 * @return 
	 *		entier valeur du coté
	 */
	public int getEdgeSize()
	{
		return edgeSize;
	}
	

	/**
	 * Accesseur: récupère la liste des fils
	 * @return 
	 *		tableau des noeuds suivants
	 */
	public FileNode[] getFiles()
	{
		return files;
	}

}

