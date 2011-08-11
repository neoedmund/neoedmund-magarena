package magic.test;

import magic.model.*;
import magic.model.phase.MagicMainPhase;

class TestFlow extends TestGameBuilder {    
    public MagicGame getGame() {
        final MagicTournament tournament=new MagicTournament();
        tournament.setDifficulty(6);
        
        final MagicPlayerProfile profile=new MagicPlayerProfile("bgruw");
        final MagicPlayerDefinition player1=new MagicPlayerDefinition("Player",false,profile,15);
        final MagicPlayerDefinition player2=new MagicPlayerDefinition("Computer",true,profile,14);
        tournament.setPlayers(new MagicPlayerDefinition[]{player1,player2});
        tournament.setStartPlayer(0);
        
        final MagicGame game=tournament.nextGame(true);
        game.setPhase(MagicMainPhase.getFirstInstance());
        final MagicPlayer player=game.getPlayer(0);
        final MagicPlayer opponent=game.getPlayer(1);

        MagicPlayer P = player;

        P.setLife(4);
        P.setPoison(0);
        addToLibrary(P,"Plains",10);
        createPermanent(game,P,"Rupture Spire",false,8);
        //createPermanent(game,P,"Grizzly Bears",false,1);
        createPermanent(game,P,"Chameleon Colossus",false,1);


        P = opponent;
        
        P.setLife(2);
        P.setPoison(0);
        addToLibrary(P,"Island",10);
        createPermanent(game,P,"Rupture Spire",false,5);
        createPermanent(game,P,"Tectonic Edge",false,3);
        createPermanent(game,P,"Grizzly Bears",false,1);
        
        return game;
    }
}
