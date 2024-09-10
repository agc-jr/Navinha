
const foguete = document.querySelector('.foguete');
const asteroid = document.querySelector('.asteroid');
const fundo = document.querySelector('.fundo');
const gameBoard = document.querySelector('.game-board');
const pontosValor = document.querySelector('.pontos-valor');
const vidasValor = document.querySelector('.vidas-valor');
const restartTela = document.querySelector('.telaRestart');
const fundoTelaFim = document.querySelector('.fundoTelaFim');
const telaFim = document.querySelector('.telaFim');
const pontuacaoFinal = document.querySelector('.pontuacaoFinalValor');
const btnRestartFinalGame = document.querySelector('.btnReiniciarJogo');
const hiscoreSuperior = document.querySelector('.hiscore-valor');

//Controle de pontuacao mais alta
if(typeof Android !== 'undefined'){
    // Chamar a função e garantir que ela seja executada após o retorno
    setTimeout(function() {
        var hiscore = Android.getHighestPontuacao();  // Chamar o método Android
        document.querySelector('.hiscoreValorFinal').textContent = hiscore;
        hiscoreSuperior.textContent = hiscore;
    }, 100);  // Aguardar 100ms para garantir que a interface esteja pronta
}else{
    cukinho = document.cookie.split("; ").find((row) => row.startsWith("hiscore="))?.split("=")[1];
    cukinho = (!cukinho)? 0 : +cukinho;
    hiscoreSuperior.textContent = cukinho;
}


const desviar = () => {
    foguete.classList.add('desvio');
    setTimeout(()=>
        {
            //alert('teste');
            foguete.classList.remove('desvio');
        }, 1000);
}

const verificaFim = setInterval(() => {
    const asteroidPosition = asteroid.offsetLeft;
    const foguetePosition = +window.getComputedStyle(foguete).bottom.replace('px', '');

    if(asteroidPosition <= 140 && foguetePosition <= 60){
        asteroid.classList.remove('asteroidAnimacao');        

        foguete.style.animation = 'none';
        foguete.style.bottom = foguetePosition+'px';

        foguete.src = './img/fogueteExplodindo.png';
        
        fundo.style.animation = 'none';        

        if(+vidasValor.textContent > 0){
            vidasValor.textContent = +vidasValor.textContent - 1;
            restartTela.style.visibility = 'visible';
        }else{            
            fundoTelaFim.style.visibility = 'visible'; 
            telaFim.style.visibility = 'visible';            
            pontuacaoFinal.textContent = +pontosValor.textContent;   
            if(typeof Android === 'undefined'){
                if(+pontuacaoFinal.textContent > +cukinho){
                    document.cookie = "hiscore="+pontuacaoFinal.textContent;
                    document.querySelector('.hiscoreValorFinal').textContent = +pontuacaoFinal.textContent;
                    hiscoreSuperior.textContent = +pontuacaoFinal.textContent;
                }else{
                    document.querySelector('.hiscoreValorFinal').textContent = (!cukinho)? 0 : +cukinho;
                } 
            }else{
                if(+pontuacaoFinal.textContent > hiscoreSuperior.textContent){ 
                    Android.savePontuacao(+pontuacaoFinal.textContent);                   
                    document.querySelector('.hiscoreValorFinal').textContent = +pontuacaoFinal.textContent;                    
                }
            }        
        }
    }else{   
        if(asteroidPosition <= 140){
            pontosValor.textContent = +pontosValor.textContent + 1;
        }   
    }
    
}, 10);

document.addEventListener('keydown', desviar);
foguete.addEventListener('touchstart', desviar);
foguete.addEventListener('click', desviar);

const restartGame = () => {
    
    restartTela.style.visibility = 'hidden';

    foguete.style.animation = '';
    
    foguete.src = './img/foguete.png';
    fundo.style.animation = 'fundo-animacao 5s infinite linear'; 
    
    asteroid.classList.add('asteroidAnimacao');


}    

const restartFinalGame = () => {
    fundoTelaFim.style.visibility = 'hidden';
    telaFim.style.visibility = 'hidden';
    pontosValor.textContent = 0;
    vidasValor.textContent = 3;
    restartGame();
}

restartTela.addEventListener('click', restartGame);
restartTela.addEventListener('touchstart', restartGame);
btnRestartFinalGame.addEventListener('click', restartFinalGame);
btnRestartFinalGame.addEventListener('touchstart', restartFinalGame);