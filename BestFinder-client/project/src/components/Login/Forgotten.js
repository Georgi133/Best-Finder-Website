import { MyNavBar } from "../Header/MyNavBar";
import { ButtonSubmit } from "./ButtonSubmit";
import style from "./Login.module.css";
import { useTranslation } from "react-i18next";

export const Forgotten = ({ setForgottenPass }) => {
  const { t } = useTranslation();

  return (
    <>
      <div className={style.overlay}>
        <div className={style.backdrop}></div>
        <div className={style.modal}>
          <div className={style.detailContainer}>
            <button
              className={style.btn}
              type="submit"
              onClick={() => setForgottenPass(false)}
            >
              X
            </button>
            <div className={style.content}>
              <div className={style.userDetails}>
                <h1 className={style.regenerationPass}>
                  {t("passRecovery")}:
                </h1>

                <form className={style.formRegPass} method="post">
                  <label className={style.emailLabel} htmlFor="email">
                  {t("userForm.email")}:
                  </label>
                  <input
                    className={style.emailInp}
                    type="email"
                    placeholder="pesho@abv.bg"
                    id="email"
                    name="email"
                  />
                  <ButtonSubmit/>
                </form>
              </div>
            </div>
          </div>
        </div>
      </div>
    </>
  );
};
