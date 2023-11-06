import { MyNavBar } from "../Header/MyNavBar";
import { useForm } from "../useForm/useForm";
import style from "./ChangeUserRole.module.css";
import { ButtonSubmit } from "../Login/ButtonSubmit";
import { useAdminContext } from "../AdminContext/AdminContext";
import { useAuthContext } from "../AuthContext/AuthContext";
import { useEffect, useState } from "react";
import { ChangeUser } from "./ChangeUser";
import { useTranslation } from "react-i18next";
import { useValidatorContext } from "../ValidatorContext/ValidatorContext";
import { ErrorMessage } from "../ErrorMessage/ErrorMessage";
import { NotAllowed } from "../NotAllowed/NotAllowed";

export const ChangeUserRole = () => {
  const { t } = useTranslation();
  const { changeRoleValidateEmail } = useValidatorContext();
  const { serverErrorsAdmin, errorMessageAdmin, isChanged, setIsChanged} = useAdminContext();
  const {
    resetPage,
    onChangeFindUserSubmit,
    foundUserEmail,
    foundUserFullName,
    foundUserRole,
  } = useAdminContext();
  const { userRole } = useAuthContext();
  const [changeRole, setChangeRole] = useState(false);

  useEffect(() => {
    return () => {
      resetPage();
      setIsChanged(false)
    };
  }, []);

  const { values, changeHandler, onSubmit, formErrors } = useForm(
    {
      email: "",
      currentUserRole: userRole,
    },
    onChangeFindUserSubmit
  );

  const onEmailSearch = (e) => {
    setIsChanged(false)
    onSubmit(e, changeRoleValidateEmail(values));
  }

  const onButtonClick = (e) => {
    e.preventDefault();
    setChangeRole(true);
    setIsChanged(false);
  }

  return (
    <>
      <MyNavBar />
      {changeRole && (
        <ChangeUser
          setChangeRole={setChangeRole}
          currentUserRole={foundUserRole}
        />
      )}
      <div className={style.container}>
        <h2 className={style.header}>{t("changeUserRole.title")}</h2>
        {isChanged && <div className={style.success}>Successfully changed!</div>}
        { isChanged ? '' : errorMessageAdmin && <ErrorMessage message={errorMessageAdmin} />}
        {!foundUserEmail && (
          <form onSubmit={onEmailSearch} className={style.innerContainer}>
            <label className={style.lyrics} htmlFor="email">
            {t("changeUserRole.title2")}
            </label>
            <input
              value={values.email}
              onChange={changeHandler}
              type="email"
              name="email"
              className={style.inp + " form-control"}
              id="email"
              min={4}
              autoComplete="true"
              placeholder="Enter email"
              required
            />
            {formErrors.email ? <ErrorMessage message={formErrors.email}/> : 
            serverErrorsAdmin.email ? <ErrorMessage message={serverErrorsAdmin.email}/>:
            ''}
            <ButtonSubmit />
          </form>
        )}
        {foundUserEmail && (
          <>
            <div className={style.found}>
              <p>
              {t("changeRoleUserFields.email")}:
                <strong className={style.innerPar}>{foundUserEmail}</strong>
              </p>
              <p>
              {t("changeRoleUserFields.name")}:
                <strong className={style.innerPar}>{foundUserFullName}</strong>
              </p>
              <p>
              {t("changeRoleUserFields.role")}:
                <strong className={style.innerPar}>{foundUserRole}</strong>
              </p>
            </div>
            <button
              onClick={onButtonClick}
              type="submit"
              className={style.loginBtn + " btn btn-primary"}
            >
              {t("changeRoleUserFields.btn")}
            </button>
          </>
        )}
      </div>
    </>
  );
};
