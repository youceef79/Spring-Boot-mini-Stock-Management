package org.sid.web;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.sid.dao.CategorieRepository;
import org.sid.dao.ClientRepository;
import org.sid.dao.EntreeRepository;
import org.sid.dao.FournisseurRepository;
import org.sid.dao.ProduitRepository;
import org.sid.dao.SortieRepository;
import org.sid.dao.UserRepository;
import org.sid.entities.Categorie;
import org.sid.entities.Client;
import org.sid.entities.Entree;
import org.sid.entities.Fournisseur;
import org.sid.entities.Produit;
import org.sid.entities.Sortie;
import org.sid.entities.User;
import org.sid.metier.IStockImplementation;
import org.sid.metier.IStockMetier;
import org.sid.sec.SecurityConfig;
import org.sid.sec.UserPrincipalDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;


@Controller
public class ProduitsController {
	@Autowired
	private ProduitRepository prodRep;
	@Autowired
	private CategorieRepository catRep;
	@Autowired
	private FournisseurRepository fourRep;
	@Autowired
	private IStockMetier sm;
	@Autowired
	private EntreeRepository eRep;
	
	@Autowired
	private ClientRepository clRep;
	
	@Autowired
	private SortieRepository srRep;
	
	@Autowired
	private UserRepository userRep;
	
	@SuppressWarnings("unused")
	@RequestMapping(value = "/prods")
	public String prods(Model model,@RequestParam(name="cat" , defaultValue = "Informatique") String cat,
			@RequestParam(name="catCh" , defaultValue = "Informatique") String catCh,
			@RequestParam(name="page" , defaultValue = "1") int page,
			@RequestParam(name="slide", defaultValue = "") String slide) {
		
		if (cat.contains(",")) {
			String[] cats = cat.split(",");
			cat = cats[1];
		}
		
		if (!catCh.equals(cat)) {
			page=1;
			slide="";
		}
		
		Categorie onec = catRep.findByName(cat);
		Pageable paging = PageRequest.of(0, 3);
		int pageNumberMax; 
		Page<Produit> listProds;  
		if (onec != null) {
			listProds = prodRep.listProds((long) onec.getId(), paging);
			pageNumberMax = listProds.getTotalPages();
			System.out.println("nim max"+pageNumberMax);
			if (slide.equals("prev") && page != 1) {
				page--;
			}
			if (slide.equals("next") && page != pageNumberMax ) {
				page++;
			} 	
		   paging = PageRequest.of(page-1, 3);
		   listProds = prodRep.listProds((long) onec.getId(), paging);
		   //ArrayList<Produit> listProdSplited = new ArrayList<Produit>();
		   /*
		   for (int i = page*5; i < (page+1)*5; i++) {
			   if (listProds.get(i) != null) {
				   listProdSplited.add(listProds.get(i));
			   }else {
				   break;
			   } 
		   }*/
		   model.addAttribute("listProds", listProds.getContent());
		   model.addAttribute("catCh", onec);
		   model.addAttribute("page", page);
		   model.addAttribute("pageMax", pageNumberMax);
		}else {
			model.addAttribute("listProds", null);
			model.addAttribute("catCh", new Categorie());
			}
		List<Categorie> categories = catRep.findAll();
		model.addAttribute("categories", categories);
			return "produits";
	}
	
	
	@RequestMapping(value = "/deleteProd")
	public String delete(Long id) {
		prodRep.deleteById(id);
		return "redirect:/prods";
	}
	
	
	@RequestMapping(value = "/FormEditProd" , method = RequestMethod.GET)
	public ModelAndView edit(ModelAndView mv,Long id) {
		mv =new ModelAndView("FormEditProd");
		
		Optional<Produit> p = prodRep.findById(id);
		if(p == null) {
		Produit pr = new Produit();
		mv.addObject("produit", pr); 
		} else {
		Categorie cate = p.get().getCategorie();
		if( eRep.findByProduct(p.get().getId()) == null) {
			mv.addObject("four", fourRep.findByName("nom fournisseur"));
		} else {
			mv.addObject("four", eRep.findByProduct(p.get().getId()).getFournisseur());
		}
		mv.addObject("cat", cate);
		List<Categorie> categories = catRep.findAll();
		mv.addObject("categories", categories);
   		List<Fournisseur> fournisseurs = fourRep.findAll();
   		mv.addObject("fournisseurs", fournisseurs);
   		mv.addObject("produit", p.get());
		}
		
		return mv;
		}
	
	
	@RequestMapping(value = "/FormEditProd" , method = RequestMethod.POST)
	public String edit(@Valid Produit produit,@Valid MultipartFile imageFile,BindingResult bindingresult,Long id, String cat
			, Model mv) throws IOException {
		
		   Optional<Produit> prod = prodRep.findById(id);
		   Categorie onec = catRep.findByName(cat);
		   
		   String fileName;
		   String uploadDir = "app-images";
		   
		   fileName = StringUtils.cleanPath(imageFile.getOriginalFilename());
		   
		   if(!fileName.isEmpty()) {
		   FileUploadService.saveFile(uploadDir, fileName, imageFile);
		   prod.get().setImage(fileName);
	        }
	
		   if (bindingresult.hasErrors()) {
		    	//mv.addObject("produit",produit);
		    	List<Categorie> categories = catRep.findAll();
				mv.addAttribute("categories", categories);
		   		List<Fournisseur> fournisseurs = fourRep.findAll();
		   		mv.addAttribute("fournisseurs", fournisseurs);
		   		mv.addAttribute("produit", prod.get()); 
		   		mv.addAttribute("cat", onec);
			     return "FormEditProd";
			   }
//	     prod.get().setPrix_achat_Ht(produit.getPrix_achat_Ht());
//	     prod.get().setQuantite(produit.getQuantite());
//	     prod.get().setMarge(produit.getMarge());
//	     prod.get().setTaux(produit.getTaux());
//	     prod.get().updateFields(produit.getPrix_achat_Ht()/produit.getQuantite());
		 prod.get().setDesignation(produit.getDesignation());
		 prod.get().setCategorie(onec);
	     prodRep.save(prod.get());
		return "redirect:/prods";
	}
	
	
	@RequestMapping(value = "/FormAjoutProd",method = RequestMethod.GET)
	public String ajoutForm(Model model) {
		model.addAttribute("produit",new Produit());
		List<Categorie> categories = catRep.findAll();
		model.addAttribute("categories", categories);
		model.addAttribute("cat", new Categorie());
		return "FormAjoutProd";
	}
	
	
	@RequestMapping(value = "/saveProd",method = RequestMethod.POST)
	public String ajout(@Valid Produit produit, BindingResult bindingResult, @Valid MultipartFile imageFile, Model model, String cat) throws IOException {
		
		Categorie onec = catRep.findByName(cat);
		String fileName = StringUtils.cleanPath(imageFile.getOriginalFilename());
 
        String uploadDir = "app-images";
 
        FileUploadService.saveFile(uploadDir, fileName, imageFile);
			if (bindingResult.hasErrors()) {
				List<Categorie> categories = catRep.findAll();
				model.addAttribute("categories", categories);
				model.addAttribute("cat", onec);
				model.addAttribute("produit", produit);
				model.addAttribute("filename", fileName);
			    return "FormAjoutProd";
			} else {
				produit.setCategorie(onec);
				produit.setImage(fileName);
				//produit.updateFields(produit.getPrix_achat_Ht()/produit.getQuantite());
				prodRep.save(produit);
				Entree en = new Entree(new Date(), produit , fourRep.findByName("nom fournisseur") , Integer.toUnsignedLong(0));
				en.setPrix(0);
				eRep.save(en);
			}

		return "redirect:/prods";
	}
	
	// **************  Fournisseurs  ****************************
	
	@RequestMapping(value = "/frnss")
	public String fourns(Model model) {
		List<Fournisseur> Frnss = fourRep.findAll();
		model.addAttribute("listFrnss", Frnss);
		return "Frnss";
	}

	@RequestMapping(value = "/deleteFour")
	public String deleteF(Long id) {
		fourRep.deleteById(id);
		return "redirect:/frnss";
	}
	
	
	@RequestMapping(value = "/editFormFour",method = RequestMethod.GET)
	public String editF(Model model,Long id) {
		Optional<Fournisseur> f = fourRep.findById(id);
		if(f == null) {
		Fournisseur fr = new Fournisseur();
		model.addAttribute("fournisseur", fr); 
		} else {
		model.addAttribute("fournisseur", f.get());
		}
		return "FormEditFour";
	}
	

	@RequestMapping(value = "/saveEditFour",method = RequestMethod.POST)
	public String editF(Model model, Long id ,@Valid Fournisseur ff ,BindingResult bindingResult) {
		Optional <Fournisseur> fr = fourRep.findById(id);
		if (bindingResult.hasErrors()) {
			return "FormEditFour";
			}
	     fr.get().setNom(ff.getNom());
	     fr.get().setAdresse(ff.getAdresse());
	     fr.get().setTel(ff.getTel()); 
	     fourRep.save(fr.get());
		return "redirect:/frnss";
	}
	
	@RequestMapping(value = "/FormAjoutFour",method = RequestMethod.GET)
	public String ajoutFormF(Model model) {
		model.addAttribute("fournisseur",new Fournisseur());
		return "FormAjoutFour";
	}
	
	
	@RequestMapping(value = "/saveFour",method = RequestMethod.POST)
	public String ajoutF(Model model,@Valid Fournisseur fournisseur, BindingResult bindingResult) {
		
			if (bindingResult.hasErrors()) {
				return "FormAjoutFour";
			}else {
			    fourRep.save(fournisseur);		
			}

		return "redirect:/frnss";
	}
	
	
	// **************  Clients  ****************************
	
		@RequestMapping(value = "/clients")
		public String clients(Model model) {
			List<Client> clients = clRep.findAll();
			model.addAttribute("listClients", clients);
			return "clients";
		}

		@RequestMapping(value = "/deleteClient")
		public String deleteC(Long id) {
			clRep.deleteById(id);
			return "redirect:/clients";
		}
		
		
		@RequestMapping(value = "/editFormClient",method = RequestMethod.GET)
		public String editC(Model model,Long id) {
			Optional<Client> c = clRep.findById(id);
			if(c == null) {
			Fournisseur cl = new Fournisseur();
			model.addAttribute("client", cl); 
			} else {
			model.addAttribute("client", c.get());
			}
			return "FormEditClient";
		}
		

		@RequestMapping(value = "/saveEditClient",method = RequestMethod.POST)
		public String editC(Model model, Long id ,@Valid Client repc ,BindingResult bindingResult) {
			Optional <Client> cl = clRep.findById(id);
			if (bindingResult.hasErrors()) {
				return "FormEditClient";
				}
		     cl.get().setNom(repc.getNom());
		     cl.get().setAdresse(repc.getAdresse());
		     cl.get().setTel(repc.getTel()); 
		   clRep.save(cl.get());
			return "redirect:/clients";
		}
		
		@RequestMapping(value = "/FormAjoutClient",method = RequestMethod.GET)
		public String ajoutFormC(Model model) {
			model.addAttribute("client",new Client());
			return "FormAjoutClient";
		}
		
		
		@RequestMapping(value = "/saveClient",method = RequestMethod.POST)
		public String ajoutC(Model model,@Valid Client client, BindingResult bindingResult) {
			
				if (bindingResult.hasErrors()) {
					return "FormAjoutClient";
				}else {
				    clRep.save(client);		
				}

			return "redirect:/clients";
		}
		
		// ************ Sorties ************** //
		
		@RequestMapping(value = "/exports")
		public String sorties(Model model,@RequestParam(name="cat" , defaultValue = "Informatique") String cat, 
				@RequestParam(name="page" , defaultValue = "1") int page,
				@RequestParam(name="catCh" , defaultValue = "Informatique") String catCh,
				@RequestParam(name="slide", defaultValue = "") String slide) {
			Categorie onec = catRep.findByName(cat);
			Pageable paging = PageRequest.of(0, 3);
			int pageNumberMax; 
			Page<Sortie> listsortis;  
			if (onec != null) {
				listsortis = srRep.listSortis((long) onec.getId(), paging);
				pageNumberMax = listsortis.getTotalPages();
				System.out.println("nim max"+pageNumberMax);
				if (slide.equals("prev") && page != 1) {
					page--;
				}
				if (slide.equals("next") && page != pageNumberMax ) {
					page++;
				} 	
			   paging = PageRequest.of(page-1, 3);
			   listsortis = srRep.listSortis((long) onec.getId(), paging);
			
			   model.addAttribute("listsortis", listsortis.getContent());
			   
			   model.addAttribute("listEmpty", listsortis.getContent().isEmpty());
			   System.out.println("empty :"+listsortis.getContent().isEmpty());
			   model.addAttribute("catCh", onec);
			   
			   
			   model.addAttribute("page", page);
			   
			   model.addAttribute("pageMax", pageNumberMax);
			   
			}else {
				model.addAttribute("listsortis", null);
				model.addAttribute("catCh", new Categorie());
				}
			List<Categorie> categories = catRep.findAll();
			model.addAttribute("categories", categories);
			
				
			return "sortiesF/sorties";
		}
		
		@RequestMapping(value = "/FormSortieCateg",method = RequestMethod.GET)
		public String formsortiec(Model model) {
			/*Categorie onec = catRep.findByName(cat);*/
		/* Pageable paging = PageRequest.of(0, 5);
			Page<Produit> listProds;  
			listProds = prodRep.listProds((long) onec.getId(), paging);*/
		    /*model.addAttribute("listProds", listProds.getContent());
		    model.addAttribute("catCh", onec);*/
			List<Categorie> categories = catRep.findAll();
			model.addAttribute("categories", categories);
		    return "sortiesF/FormSortieCat";
		}
		
		
		@RequestMapping(value = "/FormSortieProductsOfCat")
		public String formsortiepc(Model model, @RequestParam(name="cat" , defaultValue = "Informatique") String cat) {
		   Categorie onec = catRep.findByName(cat);
		   Pageable paging = PageRequest.of(0, 1000);
		   Page<Produit> listProds;  
		   listProds = prodRep.listProds((long) onec.getId(), paging);
		    model.addAttribute("listProds", listProds.getContent());
		    model.addAttribute("catCh", onec);
			List<Categorie> categories = catRep.findAll();
			model.addAttribute("categories", categories);
			List<Client> clients = clRep.findAll();
			model.addAttribute("listClients", clients);
		    return "sortiesF/FormSortieProdCat";
		}
		
		
		@SuppressWarnings("deprecation")
		@RequestMapping(value = "/Exporter")
		public String exp(Model model, @RequestParam(name="cat") String catCh, @RequestParam(name="prod") String prod 
				,@RequestParam(name="client") String cli, @RequestParam(name="quanDemandee" , defaultValue = "-1") long quanDemandee
				,@RequestParam(name="date", defaultValue = "01/01/1111") String date
				,@RequestParam(name="prix", defaultValue = "-1") long prix) {
		   Categorie onec = catRep.findByName(catCh);
		   Produit onep = prodRep.findByDes(prod);
		   Client onecl = clRep.findByName(cli);
		    if (quanDemandee >= 1) {
		    	onep.setQuantite(onep.getQuantite()-quanDemandee);
			    Sortie sr = new Sortie();
			    sr.setProduit(onep);
			    sr.setClient(onecl);
			    sr.setQuantiteEx(quanDemandee);
			    sr.setPrix(prix);
			    String[] dateSP = date.split("-");
			    date = dateSP[1]+"/"+dateSP[2]+"/"+dateSP[0];
			    sr.setDate(new Date(date));
			    //sr.setDate(new Date(Integer.parseInt(dateSP[2]),Integer.parseInt(dateSP[1]),Integer.parseInt(dateSP[0])));
			    srRep.save(sr);
		    	model.addAttribute("statut", "success");	
			}
		    model.addAttribute("catCh", onec);
			model.addAttribute("produit", onep);
			model.addAttribute("client", onecl);
			model.addAttribute("quanDemandee", quanDemandee);
		    return "sortiesF/FormSortieProdInfos";
		}
		
		
		// ************ Entrees ************** //
		
		@RequestMapping(value = "/imports")
		public String entrees(Model model,@RequestParam(name="cat" , defaultValue = "Informatique") String cat, 
				@RequestParam(name="page" , defaultValue = "1") int page,
				@RequestParam(name="catCh" , defaultValue = "Informatique") String catCh,
				@RequestParam(name="slide", defaultValue = "") String slide) {
			System.out.println("catch "+ cat);
			System.out.println("catch "+ catCh);
            
			if (cat.contains(",")) {
    			String[] cats = cat.split(",");
    			cat = cats[1];
			}
			
			if (!catCh.equals(cat)) {
				page=1;
				slide="";
			}
			
			Categorie onec = catRep.findByName(cat);
			Pageable paging = PageRequest.of(0, 3);
			int pageNumberMax; 
			Page<Entree> listentrees;  
			if (onec != null) {
				listentrees = eRep.listEntrees((long) onec.getId(), paging);
				pageNumberMax = listentrees.getTotalPages();
				System.out.println("nim max"+pageNumberMax);
				if (slide.equals("prev") && page != 1) {
					page--;
				}
				if (slide.equals("next") && page != pageNumberMax ) {
					page++;
				} 	
			   paging = PageRequest.of(page-1, 3);
			   listentrees = eRep.listEntrees((long)onec.getId(), paging);
			
			   model.addAttribute("listentrees", listentrees.getContent());
			   
			   model.addAttribute("listEmpty", listentrees.getContent().isEmpty());
			   System.out.println("empty :"+listentrees.getContent().isEmpty());
			   model.addAttribute("catCh", onec);
			   
			   
			   model.addAttribute("page", page);
			   System.out.println("page "+page);
			   
			   model.addAttribute("pageMax", pageNumberMax);
			  
			}else {
				model.addAttribute("listentrees", null);
				model.addAttribute("catCh", new Categorie());
				}
			List<Categorie> categories = catRep.findAll();
			model.addAttribute("categories", categories);
			
				
			return "entreesF/entrees";
		}
		
		@RequestMapping(value = "/FormEntreeCateg",method = RequestMethod.GET)
		public String formentreec(Model model) {
			/*Categorie onec = catRep.findByName(cat);*/
		/* Pageable paging = PageRequest.of(0, 5);
			Page<Produit> listProds;  
			listProds = prodRep.listProds((long) onec.getId(), paging);*/
		    /*model.addAttribute("listProds", listProds.getContent());
		    model.addAttribute("catCh", onec);*/
			List<Categorie> categories = catRep.findAll();
			model.addAttribute("categories", categories);
		    return "entreesF/FormEntreeCat";
		}
		
		@RequestMapping(value = "/FormEntreeProductsOfCat")
		public String formEntreeProdCat(Model model, @RequestParam(name="cat" , defaultValue = "Informatique") String cat) {
		   Categorie onec = catRep.findByName(cat);
		   Pageable paging = PageRequest.of(0, 1000);
		   Page<Produit> listProds;  
		   listProds = prodRep.listProds((long) onec.getId(), paging);
		    model.addAttribute("listProds", listProds.getContent());
		    model.addAttribute("catCh", onec);
		    return "entreesF/FormEntreeProdCat";
		}
		
		@SuppressWarnings("deprecation")
		@RequestMapping(value = "/importer")
		public String imp(Model model,@RequestParam(name="cat") String cat
				, @RequestParam(name="prod") String prod 
				,String fournisseur, @RequestParam(name="quanDemandee" , defaultValue = "-1") long quanDemandee
				,@RequestParam(name="date", defaultValue = "01/01/1111") String date
				,@RequestParam(name="prix_achat_", defaultValue = "-1") double prix_ach
				,@RequestParam(name="taux", defaultValue = "-1") double taux
				,@RequestParam(name="marge", defaultValue = "-1") double marge
				,@RequestParam(name="fournisseur", defaultValue = "") String four) {
		
		   Categorie onec = catRep.findByName(cat);
		   Produit onep = prodRep.findByDes(prod);
		   List<Fournisseur> frnss = fourRep.findAll();
		   Fournisseur fr = fourRep.findByName(four);
		   model.addAttribute("hide", "false");
		    model.addAttribute("catCh", onec);
			model.addAttribute("produit", onep);
			model.addAttribute("quanDemandee", quanDemandee);
			model.addAttribute("listFrnss", frnss);
		   
		    if (eRep.findByProduct(onep.getId()) != null) {
		    	Fournisseur f = eRep.findByProduct(onep.getId()).getFournisseur();
		    	model.addAttribute("fournisseur", f);
		    	model.addAttribute("hide", "true");
			}
		    
		    if (quanDemandee >= 1  &&  model.getAttribute("hide") == "true")  {
                onep.setQuantite(onep.getQuantite()+quanDemandee);
                String[] dateSP = date.split("-");
			    date = dateSP[1]+"/"+dateSP[2]+"/"+dateSP[0];
			    Entree en = eRep.findByProduct(onep.getId());
			    en.setProduit(onep);
			    en.setQuantite(quanDemandee);
			    en.setFournisseur(eRep.findByProduct(onep.getId()).getFournisseur());
			    en.setPrix(onep.getPrix_achat_Ht());
			    en.setDate(new Date(date));
			    eRep.save(en);
			    model.addAttribute("statut", "success");
			}
		    
		    if (quanDemandee >= 1  &&  model.getAttribute("hide") == "false")  {
                onep.setQuantite(quanDemandee);
                onep.setMarge(marge);
                onep.setTaux(taux);
                onep.updateFields(prix_ach/quanDemandee);
                String[] dateSP = date.split("-");
			    date = dateSP[1]+"/"+dateSP[2]+"/"+dateSP[0];
			    Entree en = new Entree(new Date(date) , onep , fr , quanDemandee);
			    en.setPrix(onep.getPrix_achat_Ht());
			    eRep.save(en);
			    model.addAttribute("statut", "success");
			}
		    
		    model.addAttribute("catCh", onec);
			model.addAttribute("produit", onep);
			model.addAttribute("quanDemandee", quanDemandee);
			model.addAttribute("listFrnss", frnss);
			
		    return "entreesF/FormEntreeProdInfos";
		}
		
		
		@RequestMapping(value = "/EntreeEdit" , method = RequestMethod.GET )
		public String EntreeEdit(Model model,long id) {
			
              Optional<Entree> eOpt = eRep.findById(id);
              Entree e = eOpt.get();
              Fournisseur f = e.getFournisseur();
              Produit p = e.getProduit();
              Categorie c = p.getCategorie();
              List<Fournisseur> frnss = fourRep.findAll();
		   
		    model.addAttribute("cat", c);
		    model.addAttribute("entree", e);
			model.addAttribute("produit", p);
			model.addAttribute("fournisseur", f);
			
			model.addAttribute("listFrnss", frnss);
			
		    return "entreesF/EntreeEditProdInfos";
		}
		
		@SuppressWarnings("deprecation")
		@RequestMapping(value = "/EntreeEdit" , method = RequestMethod.POST)
		public String EntreeEdit1(Model model , @RequestParam(name="entreeId") long id
				,String fournisseur, @RequestParam(name="quanDemandee" , defaultValue = "-1") long quanDemandee
				,@RequestParam(name="date", defaultValue = "01/01/1111") String date
				,@RequestParam(name="prix_achat_", defaultValue = "-1") double prix_ach
				,@RequestParam(name="taux", defaultValue = "-1") double taux
				,@RequestParam(name="marge", defaultValue = "-1") double marge
				,@RequestParam(name="fournisseur", defaultValue = "") String four) {
			
			Optional<Entree> eOp = eRep.findById(id);
			Entree e = eOp.get();
			Produit onep = e.getProduit();
			Fournisseur f = fourRep.findByName(four);
			Categorie c = onep.getCategorie();
			List<Fournisseur> frnss = fourRep.findAll();
			onep.setQuantite(quanDemandee);
            onep.setMarge(marge);
            onep.setTaux(taux);
            onep.updateFields(prix_ach/quanDemandee);
            String[] dateSP = date.split("-");
		    date = dateSP[1]+"/"+dateSP[2]+"/"+dateSP[0];
		    e.setPrix(onep.getPrix_achat_Ht());
		    e.setFournisseur(f);
		    e.setQuantite(quanDemandee);
		    e.setDate(new Date(date));
		    eRep.save(e);
		    model.addAttribute("statut", "success");
		   
		    model.addAttribute("cat", c);
		    model.addAttribute("entree", e);
			model.addAttribute("produit", onep);
			model.addAttribute("fournisseur", f);
			
			model.addAttribute("listFrnss", frnss);
			
		    return "entreesF/EntreeEditProdInfos";
		}
		
		@RequestMapping(value = "/deleteEntree")
		public String deleteEntree(long id) {
			Optional<Entree> eO = eRep.findById(id);
			Entree e = eO.get();
			Produit p = e.getProduit();
			p.setQuantite(0);
			p.setMarge(0);
			p.setTaux(0);
			p.setPrix_achat_Ht(0);
			p.setPrix_vente_Ht(0);
			p.setPrix_vente_ttc(0);
			prodRep.save(p);
			eRep.deleteById(id);
			return "redirect:/imports";
		}
		
		/*************     login , logout , register     ****************/
		
		
		@RequestMapping(value = "/register")
		public String register(Model model) {
			 return "Form/register1";
		}
		
		@RequestMapping(value = "/login")
		public String login(Model model) {
			 return "Form/login1";
		}
		@RequestMapping(value = "/404")
		public String error(Model model) {
			 return "404";
		}
		
		@RequestMapping(value = "/signup" , method = RequestMethod.POST)
		public String signup(Model model , String name , String email, String password ,String password_confirmation) {
			if ( !password.equals(password_confirmation) ||  password.equals("")) {
			
				model.addAttribute("name", name);
				model.addAttribute("email", email);
				model.addAttribute("passConfirmFailed", true);
				
				return "Form/register1";
								
			}
			User u = new User();
			u.setEmail(email);
			u.setUsername(name);
			u.setPassword(SecurityConfig.encoder().encode(password));
	        userRep.save(u);
	        
			 return "redirect:/login";
		}
		
	/************** user Informations ****************** updateUserWthPass*/
		
		@RequestMapping(value = "user/Infos")
		public String userinfos(Model model, UserPrincipalDetailsService principleService) {
		
			User user = principleService.getUser();
		    model.addAttribute("user", user);
	
		    return "user/details";
		}
		
		@RequestMapping(value = "user/updateUserWthPass" , method = RequestMethod.POST)
		public String update(Model model ,String username, String name , String email, String password,
				UserPrincipalDetailsService principleService) { 
			User user = userRep.findByUsername(username); 
			 //System.out.println("user :"+ user.getEmail());
			if ( !SecurityConfig.encoder().matches(password, user.getPassword()) ) {
			
				model.addAttribute("name", name);
				model.addAttribute("email", email);
				model.addAttribute("user", user);
				model.addAttribute("passFailed", true);
				
				return "user/details";
								
			}
			user.setEmail(email);
			user.setUsername(name);
	        userRep.save(user);
	    	model.addAttribute("passConfirmed", true);
	    	model.addAttribute("user", user);
	    	principleService.setUser(user);
			 return "user/details";
		}
			
		
}
