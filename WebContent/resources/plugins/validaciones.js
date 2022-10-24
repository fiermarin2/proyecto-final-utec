//document.oncontextmenu = function(){return false}

function imprimirPantalla(){
	if (window.print){
		window.print();
	}
}

function validoContrasena(fld, fld2) {
    var error = "";
    if (fld.value != "" && fld.value != fld2.value) {
        fld.style.background = '#F2F2F2'; 
        fld2.style.background = '#F2F2F2'; 		
        error = "Las contrase�as no coinciden\n";
    } else {
        fld.style.background = 'White';
		fld2.style.background = 'White';		
    }
    return error;  
}

function validateEmpty(fld, mensaje) {
    var error = "";
 
    if (fld.value.length == 0) {
        fld.style.background = '#F2F2F2'; 
        error = mensaje;
    } else {
        fld.style.background = 'White';
    }
    return error;  
}

function trim(s)
{
  return s.replace(/^\s+|\s+$/, '');
}
function validateEmail(fld) {
    var error="";
    var tfld = trim(fld.value);                        // value of field with whitespace trimmed off
    var emailFilter = /^[^@]+@[^@.]+\.[^@]*\w\w$/ ;
    var illegalChars= /[\(\)\<\>\,\;\:\\\"\[\]]/ ;
   
    if (fld.value == "") {
        fld.style.background = '#F2F2F2';
        error = "Ingrese un e-mail.\n";
    } else if (!emailFilter.test(tfld)) {              //test email for illegal characters
        fld.style.background = '#F2F2F2';
        error = "Por favor ingrese un e-mail existente.\n";
    } else if (fld.value.match(illegalChars)) {
        fld.style.background = '#F2F2F2';
        error = "El e-mail ingresado tiene caracteres ilegales.\n";
    } else {
        fld.style.background = 'White';
    }
    return error;
}