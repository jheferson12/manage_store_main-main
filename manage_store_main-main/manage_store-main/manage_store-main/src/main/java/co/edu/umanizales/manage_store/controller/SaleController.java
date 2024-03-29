package co.edu.umanizales.manage_store.controller;

import co.edu.umanizales.manage_store.controller.dto.ResponseDTO;
import co.edu.umanizales.manage_store.controller.dto.SaleByStoreDTO;
import co.edu.umanizales.manage_store.controller.dto.SaleDTO;
import co.edu.umanizales.manage_store.model.Sale;
import co.edu.umanizales.manage_store.model.Seller;
import co.edu.umanizales.manage_store.model.Store;
import co.edu.umanizales.manage_store.service.SaleService;
import co.edu.umanizales.manage_store.service.SellerService;
import co.edu.umanizales.manage_store.service.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path = "sale")
public class SaleController {


    @Autowired
    private SaleService saleService;
    @Autowired
    private SellerService sellerService;
    @Autowired
    private StoreService storeService;


        private Store store;
        private List<Sale> sales;
        private List<Store>storeList;
        //En este codigo lo qie hacemos es crear un constructor vacio en ese contructor vacio
        public void addStoreAverage(Store store){this.storeList.add(store);}

// En este codigo tenemos las ventas por tienda donde cree una clase que se llame SaleByStoreDTO
        @GetMapping("/salesbystorebyquantity/{quantity}")

        public ResponseEntity<ResponseDTO> getSalesByStoreByQuantity(@PathVariable int quantity) {
            List<Store> storeL = new ArrayList<>();
            //En esta parte se hace un foreach
            for (Sale i : saleService.getSales()) {
                if (saleService.getTotalSalesByStore(i.getStore().getCode())> quantity) {
                    storeL.add(i.getStore());

                }
  // Abajo del codigo vemos que lo que hacemos es pedir a que me retorne el nombre de la lista

            }
            return new ResponseEntity<>(
                    new ResponseDTO(200, storeL, null), HttpStatus.OK);


        }
    @GetMapping
    public ResponseEntity<ResponseDTO> getSales(){
        return new ResponseEntity<>(
                new ResponseDTO(200,
                        saleService.getSales(),
                null),
                HttpStatus.OK);
    }
    @GetMapping(path="/total")
    public ResponseEntity<ResponseDTO> getTotalSales(){
        return new ResponseEntity<>(
                new ResponseDTO(200, saleService.getTotalSales(),null),HttpStatus.OK);
    }

    @GetMapping(path="/total/{code}")
    public ResponseEntity<ResponseDTO> getTotalSalesBySeller(
            @PathVariable String code
    ){
        return new ResponseEntity<>(
                new ResponseDTO(200, saleService.getTotalSalesBySeller(code),null),HttpStatus.OK);
    }
    @GetMapping(path="/totalStore/{code}")
    public ResponseEntity<ResponseDTO> getTotalSalesByStore(
            @PathVariable String code
    ){
        return new ResponseEntity<>(
                new ResponseDTO(200, saleService.getTotalSalesByStore(code),null),HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ResponseDTO> createSale(@RequestBody
                                                  SaleDTO saleDTO){
        Seller findSeller = sellerService.getSellerById(saleDTO.getSellerId());
        if( findSeller == null){
            return new ResponseEntity<>(new ResponseDTO(409,
                    "El vendedor ingresado no existe",null),
                    HttpStatus.BAD_REQUEST);
        }
        Store findStore = storeService.getStoreById(saleDTO.getStoreId());
        if( findStore == null){
            return new ResponseEntity<>(new ResponseDTO(409,
                    "La sucursal ingresada no existe",null),
                    HttpStatus.BAD_REQUEST);
        }
        saleService.addSale(new Sale(findStore,findSeller,
                saleDTO.getQuantity()));
        return new ResponseEntity<>(new ResponseDTO(200,
                "Venta adicionada",null),
                HttpStatus.OK);
    }

        @GetMapping(path = "/bestseller")
        public ResponseEntity<ResponseDTO> getBestSeller(){
            return new ResponseEntity<>(new ResponseDTO(200,
                    saleService.getBestSeller(sellerService.getSellers()), null),HttpStatus.OK);
        }
    @GetMapping(path = "/bestStore")
    public ResponseEntity<ResponseDTO> getBestStore(){
        return new ResponseEntity<>( new ResponseDTO(
                200, (saleService.getBestStore(storeService.getStores())),null),HttpStatus.OK);
    }
    @GetMapping(path = "/averagesalebyseller")
    public ResponseEntity<ResponseDTO> getAverageSaleBySeller(){
        int quantity=saleService.getTotalSales();
        if(quantity==0){
            return new ResponseEntity<>(new ResponseDTO(409,
                    "no existen ventas,por lo tanto no se puede realizar a cabo un promedio  ",null),
                    HttpStatus.BAD_REQUEST);


        }
        else {
            return new ResponseEntity<>( new ResponseDTO(
                    200, saleService.getTotalSales()/(float)sellerService.getSellers().size(),
                    null),HttpStatus.OK);
        }

    }

    @GetMapping(path = "/averagesalebystore")
    public ResponseEntity<ResponseDTO> getAverageSaleByStore() {
        int average = saleService.getTotalSales();
        int quantitystore=storeService.getStores().size();
        if (average == 0) {
            return new ResponseEntity<>(new ResponseDTO(409,
                    "no existen ventas,por lo tanto no se puede realizar a cabo un promedio  ", null),
                    HttpStatus.BAD_REQUEST);


        } else {
            return new ResponseEntity<>(new ResponseDTO(
                    200,average/quantitystore, null), HttpStatus.OK);
        }

    }
    @GetMapping(path = "/storescant/{cant}")
    public ResponseEntity<ResponseDTO> getStoresByCant (@PathVariable int cant) {
        if (saleService.getStoresByCant(cant).isEmpty()) {
            return new ResponseEntity<>(new ResponseDTO(
                    404, "No existen tiendas para cumplir con el requisito pedido", null)
                    ,HttpStatus.BAD_REQUEST);
        }
        else {
            return new ResponseEntity<>(new ResponseDTO(200,saleService.getStoresByCant(cant),null),HttpStatus.OK);
        }
    }
    
}

