import { useTranslation } from "react-i18next";
import { useAuthContext } from "../AuthContext/AuthContext";
import { MyNavBar } from "../Header/MyNavBar";
import { useForm } from "../useForm/useForm";
import style from "./ChangePassword.module.css";
import { ButtonSubmit } from "../Login/ButtonSubmit";

export const ChangePassword = () => {
  const { onChangePasswordSubmit, userEmail} = useAuthContext();

  const { t } = useTranslation();

  const { values, changeHandler, onSubmit } = useForm(
    {
      currentPassword: "",
      newPassword: "",
      confirmPassword: "",
      email: userEmail,
    },
    onChangePasswordSubmit
  );

  return (
    <>
      <MyNavBar />
      <div className={style.container}>
      <form className={style.formContainer} onSubmit={onSubmit}>
        <div className={style.login + " " + "form-group"}>
          <label className={style.lyrics} htmlFor="currentPassword">
          {t("changePasswordPage.currentPass")}:
          </label>
          <input
            value={values.currentPassword}
            onChange={changeHandler}
            type="password"
            className={style.inp + " form-control"}
            id="currentPassword"
            name="currentPassword"
            placeholder="Old Password"
            required
            min={3}
          />
        </div>
        <div className={style.login + " " + "form-group"}>
          <label className={style.lyrics} htmlFor="newPassword">
          {t("changePasswordPage.newPass")}:
          </label>
          <input
            value={values.newPassword}
            onChange={changeHandler}
            type="password"
            className={style.inp + " form-control"}
            id="newPassword"
            name="newPassword"
            placeholder="New Password"
            required
            min={3}
          />
        </div>
        <div className={style.login + " " + "form-group"}>
          <label className={style.lyrics} htmlFor="confirmPassword">
          {t("changePasswordPage.confirmNewPass")}:
          </label>
          <input
            value={values.confirmPassword}
            onChange={changeHandler}
            type="password"
            className={style.inp + " form-control"}
            id="confirmPassword"
            name="confirmPassword"
            placeholder="Confirm Password"
            required
            min={3}
          />
        </div>
        <ButtonSubmit/>
      </form>
      </div>
    </>
  );
};
