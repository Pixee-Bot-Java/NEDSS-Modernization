import {
    Alert,
    Button,
    ButtonGroup,
    Grid,
    Icon,
    Modal,
    ModalFooter,
    ModalHeading,
    ModalRef,
    ModalToggleButton
} from '@trussworks/react-uswds';
import './style.scss';
import { useContext, useEffect, useRef, useState } from 'react';
import { useParams } from 'react-router-dom';
import {
    FindPatientsByFilterQuery,
    useFindContactsForPatientLazyQuery,
    useFindDocumentsForPatientLazyQuery,
    useFindInvestigationsByFilterLazyQuery,
    useFindLabReportsByFilterLazyQuery,
    useFindMorbidtyReportForPatientLazyQuery,
    useFindPatientByIdLazyQuery
} from '../../generated/graphql/schema';
import { calculateAge } from '../../utils/util';
import { Summary } from './Summary';
import { Events } from './Events';
import { Demographics } from './Demographics';
import { SearchCriteriaContext } from 'providers/SearchCriteriaContext';
import { Config } from 'config';

enum ACTIVE_TAB {
    SUMMARY = 'Summary',
    EVENT = 'Events',
    DEMOGRAPHICS = 'Demographics'
}

export const PatientProfile = () => {
    const { id } = useParams();

    const modalRef = useRef<ModalRef>(null);

    const [getPatientInvestigationData, { data: investigationData }] = useFindInvestigationsByFilterLazyQuery();
    const [getPatientLabReportData, { data: labReportData }] = useFindLabReportsByFilterLazyQuery();
    // const [getPatientProfileData, { data: patientProfileData }] = useFindPatientsByFilterLazyQuery();
    const [getMorbidityData, { data: morbidityData }] = useFindMorbidtyReportForPatientLazyQuery();
    const [getDocumentsData, { data: documentsData }] = useFindDocumentsForPatientLazyQuery();
    const [getContactsData, { data: contactsData }] = useFindContactsForPatientLazyQuery();

    const [getPatientProfileDataById, { data: patientProfileData }] = useFindPatientByIdLazyQuery();

    const [activeTab, setActiveTab] = useState<ACTIVE_TAB.DEMOGRAPHICS | ACTIVE_TAB.EVENT | ACTIVE_TAB.SUMMARY>(
        ACTIVE_TAB.SUMMARY
    );
    const [profileData, setProfileData] = useState<FindPatientsByFilterQuery['findPatientsByFilter']['content'][0]>();

    const { searchCriteria } = useContext(SearchCriteriaContext);
    const openPrintableView = () => {
        window.open(
            `${Config.nbsUrl}/LoadViewFile1.do?method=ViewFile&ContextAction=print&uid=${id}`,
            '_blank',
            'noreferrer'
        );
    };

    useEffect(() => {
        if (id) {
            getPatientProfileDataById({
                variables: {
                    id: id
                }
            });
        }
    }, []);

    const [ethnicity, setEthnicity] = useState<string>('');
    const [race, setRace] = useState<any>(undefined);
    useEffect(() => {
        if (patientProfileData?.findPatientById) {
            setProfileData(patientProfileData?.findPatientById);
            if (patientProfileData.findPatientById.id) {
                getPatientInvestigationData({
                    variables: {
                        filter: {
                            patientId: +patientProfileData.findPatientById.id
                        }
                    }
                });
                getPatientLabReportData({
                    variables: {
                        filter: {
                            patientId: +patientProfileData.findPatientById.id
                        }
                    }
                });
                getMorbidityData({
                    variables: {
                        patientId: +patientProfileData.findPatientById.id
                    }
                });
                getDocumentsData({
                    variables: {
                        patient: patientProfileData.findPatientById.id
                    }
                });
                getContactsData({
                    variables: {
                        patient: patientProfileData.findPatientById.id
                    }
                });

                searchCriteria.ethnicities.map((ethinicity) => {
                    if (ethinicity.id.code === patientProfileData?.findPatientById?.ethnicGroupInd) {
                        setEthnicity(ethinicity.codeDescTxt);
                    }
                });

                const raceArr: any = [];
                searchCriteria.races.map((race) => {
                    patientProfileData?.findPatientById?.races?.map((item: any) => {
                        if (race.id.code === item?.raceCd) {
                            raceArr.push(race.codeDescTxt);
                            setRace(raceArr);
                        }
                    });
                });
            }
        }
    }, [patientProfileData]);

    const OrderedData = ({ data, type }: any) => {
        return (
            <div>
                <h5 className="margin-0 text-normal text-gray-50 margin-bottom-05">{type}</h5>
                {data && data.length > 0 ? (
                    data.map((add: string, ind: number) => (
                        <p
                            key={ind}
                            className="margin-0 font-sans-2xs text-normal"
                            style={{
                                wordBreak: 'break-word',
                                paddingRight: '15px',
                                maxWidth: type === 'EMAIL' ? '165px' : 'auto'
                            }}>
                            {add}
                        </p>
                    ))
                ) : (
                    <p className="text-italic margin-0 text-gray-30">No Data</p>
                )}
            </div>
        );
    };

    const newOrderPhone = (data: any) => {
        const numbers: any = [];
        data?.map((item: any) => item.locator.phoneNbrTxt && numbers.push(item.locator.phoneNbrTxt));
        return <OrderedData data={numbers} type="PHONE NUMBER" />;
    };

    const newOrderEmail = (data: any) => {
        const emails: any = [];
        data?.map((item: any) => item.locator.emailAddress && emails.push(item.locator.emailAddress));
        return <OrderedData data={emails} type="EMAIL" />;
    };

    const newOrderAddress = (data: any) => {
        const address: any = [];
        data?.map(
            (item: any) =>
                item.classCd === 'PST' &&
                address.push(
                    `${item.locator.streetAddr1 ?? ''} ${item.locator.cityCd ?? ''} ${item.locator.stateCd ?? ''} ${
                        item.locator.zipCd ?? ''
                    } ${item.locator.cntryCd ?? ''}`
                )
        );
        return <OrderedData data={address} type="ADDRESS" />;
    };

    const [submittedSuccess, setSubmittedSuccess] = useState<boolean>(false);
    const [addedItem, setAddedItem] = useState<string>('');
    const [alertType, setAlertType] = useState<'error' | 'success' | 'warning' | 'info'>('success');

    useEffect(() => {
        if (submittedSuccess) {
            setTimeout(() => {
                setSubmittedSuccess(false);
            }, 5000);
        }
    }, [submittedSuccess]);

    return (
        <div className="height-full main-banner">
            <div className="bg-white grid-row flex-align-center flex-justify border-bottom-style">
                <h1 className="font-sans-xl text-medium">Patient Profile</h1>
                <div>
                    <Button type={'button'} className="display-inline-flex print-btn" onClick={openPrintableView}>
                        <Icon.Print className="margin-right-05" />
                        Print
                    </Button>
                    <ModalToggleButton
                        modalRef={modalRef}
                        opener
                        className="delete-btn display-inline-flex"
                        type={'submit'}>
                        <Icon.Delete className="margin-right-05" />
                        Delete Patient
                    </ModalToggleButton>
                    <Modal
                        ref={modalRef}
                        id="example-modal-1"
                        aria-labelledby="modal-1-heading"
                        className="padding-0"
                        aria-describedby="modal-1-description">
                        <ModalHeading
                            id="modal-1-heading"
                            className="border-bottom border-base-lighter font-sans-lg padding-2">
                            Permanently delete patient?
                        </ModalHeading>
                        <div className="margin-2 grid-row flex-no-wrap border-left-1 border-accent-warm flex-align-center">
                            <Icon.Warning className="font-sans-2xl margin-x-2" />
                            <p id="modal-1-description">
                                Would you like to permenantly delete patient record {profileData?.localId},{' '}
                                {`${profileData?.lastNm}, ${profileData?.firstNm}`}?
                            </p>
                        </div>
                        <ModalFooter className="border-top border-base-lighter padding-2 margin-left-auto">
                            <ButtonGroup>
                                <ModalToggleButton outline modalRef={modalRef} closer>
                                    No, go back
                                </ModalToggleButton>
                                <ModalToggleButton modalRef={modalRef} closer className="padding-105 text-center">
                                    Yes, delete
                                </ModalToggleButton>
                            </ButtonGroup>
                        </ModalFooter>
                    </Modal>
                </div>
            </div>
            <div className="main-body">
                <div className="margin-y-2 flex-row common-card">
                    <div className="grid-row flex-align-center flex-justify padding-2 border-bottom border-base-lighter">
                        <p className="font-sans-xl text-bold margin-0">
                            {`${profileData?.lastNm}, ${profileData?.firstNm}`}
                        </p>
                        <h5 className="font-sans-md text-medium margin-0">Patient ID: {profileData?.localId}</h5>
                    </div>
                    <Grid row gap={3} className="padding-3">
                        <Grid row col={3}>
                            <Grid col={12}>
                                <h5 className="margin-0 text-normal font-sans-1xs text-gray-50 margin-right-1">SEX</h5>
                                <p className="margin-0 font-sans-1xs text-normal">
                                    {profileData?.currSexCd === 'M'
                                        ? 'Male'
                                        : profileData?.currSexCd === 'F'
                                        ? 'Female'
                                        : 'Unknown'}
                                </p>
                            </Grid>
                            <Grid col={12} className="margin-top-3">
                                <h5 className="margin-0 text-normal font-sans-1xs text-gray-50 margin-right-1">
                                    DATE OF BIRTH
                                </h5>
                                <p className="margin-0 font-sans-1xs text-normal">
                                    {new Date(profileData?.birthTime).toLocaleDateString('en-US', {
                                        timeZone: 'UTC'
                                    })}{' '}
                                    ({calculateAge(new Date(profileData?.birthTime))})
                                </p>
                            </Grid>
                        </Grid>

                        <Grid row col={3}>
                            <Grid col={12}>{newOrderPhone(profileData?.nbsEntity?.entityLocatorParticipations)}</Grid>
                            <Grid col={12} className="margin-top-3">
                                {newOrderEmail(profileData?.nbsEntity?.entityLocatorParticipations)}
                            </Grid>
                        </Grid>

                        <Grid row col={3}>
                            <Grid col={12}>{newOrderAddress(profileData?.nbsEntity?.entityLocatorParticipations)}</Grid>
                        </Grid>

                        <Grid row col={3}>
                            <Grid col={12}>
                                <h5 className="margin-0 text-normal font-sans-1xs text-gray-50 margin-right-1">RACE</h5>
                                <p className="margin-0 font-sans-1xs text-normal">
                                    {race?.map((item: any) => item) || 'No data'}
                                </p>
                            </Grid>
                            <Grid col={12} className="margin-top-3">
                                <h5 className="margin-0 text-normal font-sans-1xs text-gray-50 margin-right-1">
                                    ETHNICITY
                                </h5>
                                <p className="margin-0 font-sans-1xs text-normal">{ethnicity}</p>
                            </Grid>
                        </Grid>
                    </Grid>
                </div>

                <div className="grid-row flex-align-center">
                    <h6
                        className={`${
                            activeTab === ACTIVE_TAB.SUMMARY && 'active'
                        } text-normal type margin-y-3 font-sans-md padding-bottom-1 cursor-pointer margin-top-2 margin-bottom-0`}
                        onClick={() => setActiveTab(ACTIVE_TAB.SUMMARY)}>
                        {ACTIVE_TAB.SUMMARY}
                    </h6>
                    <h6
                        className={`${
                            activeTab === ACTIVE_TAB.EVENT && 'active'
                        } padding-bottom-1 type text-normal margin-y-3 font-sans-md margin-x-3 cursor-pointer margin-top-2 margin-bottom-0`}
                        onClick={() => setActiveTab(ACTIVE_TAB.EVENT)}>
                        {ACTIVE_TAB.EVENT}
                    </h6>
                    <h6
                        className={`${
                            activeTab === ACTIVE_TAB.DEMOGRAPHICS && 'active'
                        } text-normal type margin-y-3 font-sans-md padding-bottom-1 cursor-pointer margin-top-2 margin-bottom-0`}
                        onClick={() => setActiveTab(ACTIVE_TAB.DEMOGRAPHICS)}>
                        {ACTIVE_TAB.DEMOGRAPHICS}
                    </h6>
                </div>

                {activeTab === ACTIVE_TAB.SUMMARY && <Summary profileData={profileData} />}
                {activeTab === ACTIVE_TAB.EVENT && (
                    <Events
                        patient={id}
                        investigationData={investigationData?.findInvestigationsByFilter}
                        labReports={labReportData?.findLabReportsByFilter}
                        morbidityData={morbidityData?.findMorbidtyReportForPatient}
                        documentsData={documentsData?.findDocumentsForPatient}
                        contactsData={contactsData?.findContactsForPatient}
                        profileData={profileData}
                    />
                )}
                {activeTab === ACTIVE_TAB.DEMOGRAPHICS && (
                    <Demographics
                        handleFormSubmission={(type: 'error' | 'success' | 'warning' | 'info', message: string) => {
                            setSubmittedSuccess(true);
                            setAddedItem(message);
                            setAlertType(type);
                        }}
                        patientProfileData={patientProfileData?.findPatientById}
                        ethnicity={ethnicity}
                    />
                )}

                <div className="text-center margin-y-5">
                    <Button outline type={'button'} onClick={() => window.scrollTo({ top: 0, behavior: 'smooth' })}>
                        <Icon.ArrowUpward className="margin-right-1" />
                        Back to top
                    </Button>
                </div>
            </div>

            {submittedSuccess && (
                <Alert
                    type={alertType}
                    heading="Success"
                    headingLevel="h4"
                    cta={
                        <Button type="button" unstyled>
                            <Icon.Close />
                        </Button>
                    }>
                    Added new name, {addedItem}
                </Alert>
            )}
        </div>
    );
};
