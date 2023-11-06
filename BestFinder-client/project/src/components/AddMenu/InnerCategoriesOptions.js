import { useTranslation } from "react-i18next";

export const InnerCategoriesOptions = () => {

  const { t } = useTranslation();
  return (
    <>
      <option hidden value="categ">
        {t("torrentCommon.category")}
      </option>
      <option value="action">ACTION</option>
      <option value="comedy">COMEDY</option>
      <option value="drama">DRAMA</option>
      <option value="romance">ROMANCE</option>
      <option value="horror">HORROR</option>
      <option value="fantasy">FANTASY</option>
      <option value="adventure">ADVENTURE</option>
      <option value="crime">CRIME</option>
    </>
  );
};
