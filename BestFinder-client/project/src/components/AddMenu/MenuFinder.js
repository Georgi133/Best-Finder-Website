import { Anime } from "./Anime"
import { Game } from "./Game"
import { Joke } from "./Joke"
import { Movie } from "./Movie"
import { Serial } from "./Serial"
import { Song } from "./Song"

export const MenuFinder = ({
    category,
}) => {

    const getCorrectMenu = (category) => {
        if(category === "anime") {
            return <Anime torrent={category}/> 
        } else if(category === "movie") {
            return <Movie torrent={category}/> 
        }else if(category === "serial") {
            return <Serial torrent={category}/>  
        }else if(category === "joke") {
            return <Joke torrent={category}/>   
        }else if(category === "game") {
            return <Game torrent={category}/>
        }else if(category === "song") {
            return <Song torrent={category}/>
        }
    }

    return(
        getCorrectMenu(category)
    )
}