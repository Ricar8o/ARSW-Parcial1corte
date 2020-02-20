package edu.eci.arsw.exams.moneylaunderingapi;

import java.util.List;
import java.util.Set;

import com.google.gson.Gson;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import edu.eci.arsw.exams.moneylaunderingapi.model.SuspectAccount;
import edu.eci.arsw.exams.moneylaunderingapi.service.MoneyLaunderingService;

@RestController
public class MoneyLaunderingController
{
    @Autowired
    MoneyLaunderingService moneyLaunderingService;

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @RequestMapping( value = "/fraud-bank-accounts", method = RequestMethod.GET)
    public ResponseEntity<String> offendingAccounts() {
        //System.out.println("Hola");
        ResponseEntity r = null;
        try{
            List<SuspectAccount> sa = moneyLaunderingService.getSuspectAccounts();
            Gson gson = new Gson();
			String json = gson.toJson(sa);
			r = new ResponseEntity<>(json, HttpStatus.OK);
		} catch (Exception e){
			r = new ResponseEntity<>("Cuenta no encontrada", HttpStatus.NOT_FOUND);
		}
        return r;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/{cuenta}" , method = RequestMethod.GET)
	public ResponseEntity<String> getCuenta(@PathVariable String cuenta)  {
		ResponseEntity r = null;
		try{
			List<SuspectAccount> sa = moneyLaunderingService.getSuspectAccount(cuenta);
			Gson gson = new Gson();
			String json = gson.toJson(sa);
			r = new ResponseEntity<>(json, HttpStatus.OK);
		} catch (Exception e){
			r = new ResponseEntity<>("Cuenta no encontrada", HttpStatus.NOT_FOUND);
		}
		return r;
    }
    

}
