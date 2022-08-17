import Axios, { AxiosPromise, AxiosRequestConfig } from "axios";
import { getSite } from "../../hooks/useSite";

const cybersourceService = {
    getFlexKey(): AxiosPromise<any> {
        const storeID = getSite()?.storeID;
        const requestOptions: AxiosRequestConfig = Object.assign({
            url: "/wcs/resources/storeId/" + storeID + "/cybersource/flexkey",
            method: "POST",
            data: {
                "encryptionType": "None"
            }
        });
        return Axios(requestOptions);
    },

    createFlexToken(obj, host) {
        const requestOptions: AxiosRequestConfig = Object.assign({
            url: "https://"+ host +"/flex/v1/tokens",
            method: "POST",
            data: obj
        });
        return Axios(requestOptions);
    },
};

export default cybersourceService;
