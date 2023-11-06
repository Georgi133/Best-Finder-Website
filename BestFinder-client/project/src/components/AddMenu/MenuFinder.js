import { Anime } from "./Anime"
import { Game } from "./Game"
import { Joke } from "./Joke"
import { Movie } from "./Movie"
import { Serial } from "./Serial"
import { Song } from "./Song"

export const MenuFinder = ({
    category,
    setValueCategory,
}) => {

    const getCorrectMenu = (category) => {
        if(category === "anime") {
            return <Anime torrent={category} setValueCategory={setValueCategory}/> 
        } else if(category === "movie") {
            return <Movie torrent={category} setValueCategory={setValueCategory}/> 
        }else if(category === "serial") {
            return <Serial torrent={category} setValueCategory={setValueCategory}/>  
        }else if(category === "joke") {
            return <Joke torrent={category} setValueCategory={setValueCategory}/>   
        }else if(category === "game") {
            return <Game torrent={category} setValueCategory={setValueCategory}/>
        }else if(category === "song") {
            return <Song torrent={category} setValueCategory={setValueCategory}/>
        }
    }

    return(
        getCorrectMenu(category)
    )
}