package magic.model; 

public enum MagicLayer {
    Copy,        //1.  copy 
    Control,     //2.  control changing
    Text,        //3.  text changing
    CDASubtype,  //4a. CDA subtype
    Type,        //4b. type-changing (include sub and super types)
    CDAColor,    //5a. CDA color
    Color,       //5b. color changing
    Ability,     //6.  ability adding/removing
    CDAPT,       //7a. CDA p/t
    SetPT,       //7b. set p/t to specific value
    ModPT,       //7c. modify p/t
    CountersPT,  //7d. p/t changes due to counters
    SwitchPT,    //7e. switch p/t 
    Player,      //8.  affect player, not objects
    Game,        //9.  affect game rules, not objects
    ;
}
