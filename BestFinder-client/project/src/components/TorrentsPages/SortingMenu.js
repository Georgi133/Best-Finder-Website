import { useTorrentContext } from "../TorrentContext.js/TorrentContext";
import style from "./Pages.module.css";

export const SortingMenu = ({
    category,
    torrentInfoSortByYear,
    seasons,
}) => {

    const { onSortChange , setSelectorValue, selectorValue } = useTorrentContext()


    const onSort = (e) => {
        const value = e.target.value;
        setSelectorValue(value);
        if(value === 'year') {
          if(torrentInfoSortByYear.length === 0) {
          onSortChange(category, seasons)
          } 
        }
  
      }

    return(
        <select
        value={selectorValue}
        className={style.selectCl}
        onChange={onSort}>
        
        <option 
        className={style.opt}
         value="likes"
         >
        Sorted by likes
        </option>
        <option className={style.opt} value="year">
        Sorted by {seasons ? seasons : 'year'}
        </option>
      </select>
    )
}