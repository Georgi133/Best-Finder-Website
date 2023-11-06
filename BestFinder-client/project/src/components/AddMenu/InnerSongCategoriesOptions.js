import { useTranslation } from "react-i18next";

export const InnerSongCategoriesOptions = () => {

  const { t } = useTranslation();
    return(
     <>
     <option hidden value="categ">
     {t("torrentCommon.category")}
      </option>
      <option value="pop">POP</option>
      <option value="rock">ROCK</option>
      <option value="hip_hop">HIP_HOP</option>
      <option value="jazz">JAZZ</option>
      <option value="blues">BLUES</option>
      <option value="classical">CLASSICAL</option>
      <option value="metal">METAL</option>
     </>
        
    )
}