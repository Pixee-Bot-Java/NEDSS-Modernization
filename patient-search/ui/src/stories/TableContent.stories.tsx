import { ComponentStory, ComponentMeta } from '@storybook/react';
import { TableContent } from '../components/TableContent/TableContent';

export default {
    title: 'Components/TableContent',
    component: TableContent
} as ComponentMeta<typeof TableContent>;

const Template: ComponentStory<typeof TableContent> = (args) => <TableContent {...args} />;

export const BasicTableContent = Template.bind({});
BasicTableContent.args = {
    tableHead: [
        { name: 'Person', sortable: true },
        { name: 'Date of birth', sortable: false },
        { name: 'Type', sortable: true },
        { name: 'Last test', sortable: true },
        { name: 'Last result', sortable: true },
        { name: 'Action', sortable: false }
    ],

    tableBody: {
        content: [
            {
                id: '10000001',
                nbsEntity: {
                    entityLocatorParticipations: [
                        {
                            classCd: 'PST',
                            locator: {
                                emailAddress: null,
                                extenstionTxt: null,
                                phoneNbrTxt: null,
                                urlAddress: null,
                                censusBlockCd: null,
                                censusMinorCivilDivisionCd: null,
                                censusTrackCd: null,
                                cityCd: null,
                                cityDescTxt: 'Atlanta',
                                cntryCd: '840',
                                cntryDescTxt: null,
                                cntyCd: '13135',
                                cntyDescTxt: null,
                                msaCongressDistrictCd: null,
                                regionDistrictCd: null,
                                stateCd: '13',
                                streetAddr1: '123 Main St.',
                                streetAddr2: null,
                                zipCd: '30024',
                                geocodeMatchInd: null,
                                withinCityLimitsInd: null,
                                censusTract: null,
                                __typename: 'Locator'
                            },
                            __typename: 'LocatorParticipations'
                        },
                        {
                            classCd: 'TELE',
                            locator: {
                                emailAddress: null,
                                extenstionTxt: null,
                                phoneNbrTxt: '232-322-2222',
                                urlAddress: null,
                                censusBlockCd: null,
                                censusMinorCivilDivisionCd: null,
                                censusTrackCd: null,
                                cityCd: null,
                                cityDescTxt: null,
                                cntryCd: null,
                                cntryDescTxt: null,
                                cntyCd: null,
                                cntyDescTxt: null,
                                msaCongressDistrictCd: null,
                                regionDistrictCd: null,
                                stateCd: null,
                                streetAddr1: null,
                                streetAddr2: null,
                                zipCd: null,
                                geocodeMatchInd: null,
                                withinCityLimitsInd: null,
                                censusTract: null,
                                __typename: 'Locator'
                            },
                            __typename: 'LocatorParticipations'
                        },
                        {
                            classCd: 'TELE',
                            locator: {
                                emailAddress: null,
                                extenstionTxt: null,
                                phoneNbrTxt: '456-232-3222',
                                urlAddress: null,
                                censusBlockCd: null,
                                censusMinorCivilDivisionCd: null,
                                censusTrackCd: null,
                                cityCd: null,
                                cityDescTxt: null,
                                cntryCd: null,
                                cntryDescTxt: null,
                                cntyCd: null,
                                cntyDescTxt: null,
                                msaCongressDistrictCd: null,
                                regionDistrictCd: null,
                                stateCd: null,
                                streetAddr1: null,
                                streetAddr2: null,
                                zipCd: null,
                                geocodeMatchInd: null,
                                withinCityLimitsInd: null,
                                censusTract: null,
                                __typename: 'Locator'
                            },
                            __typename: 'LocatorParticipations'
                        },
                        {
                            classCd: 'TELE',
                            locator: {
                                emailAddress: null,
                                extenstionTxt: null,
                                phoneNbrTxt: '232-322-2222',
                                urlAddress: null,
                                censusBlockCd: null,
                                censusMinorCivilDivisionCd: null,
                                censusTrackCd: null,
                                cityCd: null,
                                cityDescTxt: null,
                                cntryCd: null,
                                cntryDescTxt: null,
                                cntyCd: null,
                                cntyDescTxt: null,
                                msaCongressDistrictCd: null,
                                regionDistrictCd: null,
                                stateCd: null,
                                streetAddr1: null,
                                streetAddr2: null,
                                zipCd: null,
                                geocodeMatchInd: null,
                                withinCityLimitsInd: null,
                                censusTract: null,
                                __typename: 'Locator'
                            },
                            __typename: 'LocatorParticipations'
                        },
                        {
                            classCd: 'TELE',
                            locator: {
                                emailAddress: 'fdsfs@dsds.com',
                                extenstionTxt: null,
                                phoneNbrTxt: null,
                                urlAddress: null,
                                censusBlockCd: null,
                                censusMinorCivilDivisionCd: null,
                                censusTrackCd: null,
                                cityCd: null,
                                cityDescTxt: null,
                                cntryCd: null,
                                cntryDescTxt: null,
                                cntyCd: null,
                                cntyDescTxt: null,
                                msaCongressDistrictCd: null,
                                regionDistrictCd: null,
                                stateCd: null,
                                streetAddr1: null,
                                streetAddr2: null,
                                zipCd: null,
                                geocodeMatchInd: null,
                                withinCityLimitsInd: null,
                                censusTract: null,
                                __typename: 'Locator'
                            },
                            __typename: 'LocatorParticipations'
                        }
                    ],
                    __typename: 'NBSEntity'
                },
                entityIds: [
                    {
                        typeDescTxt: 'Account number',
                        typeCd: 'ACCOUNT_NUMBER',
                        rootExtensionTxt: '3453453533',
                        assigningAuthorityCd: 'GA',
                        assigningAuthorityDescTxt: 'Georgia',
                        __typename: 'PersonIdentification'
                    }
                ],
                names: [
                    {
                        firstNm: 'Surma',
                        middleNm: 'J',
                        lastNm: 'Singh',
                        nmSuffix: null,
                        nmPrefix: null,
                        __typename: 'PersonName'
                    }
                ],
                addReasonCd: null,
                addTime: '2015-09-22T14:40:46.137Z',
                addUserId: '10000000',
                administrativeGenderCd: null,
                ageCalc: null,
                ageCalcTime: null,
                ageCalcUnitCd: null,
                ageCategoryCd: null,
                ageReported: null,
                ageReportedTime: null,
                ageReportedUnitCd: null,
                birthGenderCd: null,
                birthOrderNbr: null,
                birthTime: '1990-01-01T00:00:00Z',
                birthTimeCalc: '1990-01-01T00:00:00Z',
                cd: 'PAT',
                cdDescTxt: null,
                currSexCd: 'M',
                deceasedIndCd: 'N',
                deceasedTime: null,
                description: null,
                educationLevelCd: null,
                educationLevelDescTxt: null,
                ethnicGroupInd: 'NOT_HISPANIC_OR_LATINO',
                lastChgReasonCd: null,
                lastChgTime: '2015-10-14T22:51:43.107Z',
                lastChgUserId: '10000000',
                localId: 'PSN10063000GA01',
                maritalStatusCd: 'M',
                maritalStatusDescTxt: null,
                mothersMaidenNm: null,
                multipleBirthInd: null,
                occupationCd: null,
                preferredGenderCd: null,
                primLangCd: null,
                primLangDescTxt: null,
                recordStatusTime: '2015-10-14T22:51:43.107Z',
                statusCd: 'A',
                statusTime: '2015-10-14T22:51:43.107Z',
                survivedIndCd: null,
                userAffiliationTxt: null,
                firstNm: 'Surma',
                lastNm: 'Singh',
                middleNm: 'J',
                nmPrefix: null,
                nmSuffix: null,
                preferredNm: null,
                hmStreetAddr1: null,
                hmStreetAddr2: null,
                hmCityCd: null,
                hmCityDescTxt: null,
                hmStateCd: null,
                hmZipCd: null,
                hmCntyCd: null,
                hmCntryCd: null,
                hmPhoneNbr: null,
                hmPhoneCntryCd: null,
                hmEmailAddr: null,
                cellPhoneNbr: null,
                wkStreetAddr1: null,
                wkStreetAddr2: null,
                wkCityCd: null,
                wkCityDescTxt: null,
                wkStateCd: null,
                wkZipCd: null,
                wkCntyCd: null,
                wkCntryCd: null,
                wkPhoneNbr: null,
                wkPhoneCntryCd: null,
                wkEmailAddr: null,
                ssn: null,
                medicaidNum: null,
                dlNum: null,
                dlStateCd: null,
                raceCd: null,
                raceSeqNbr: null,
                raceCategoryCd: null,
                ethnicityGroupCd: null,
                ethnicGroupSeqNbr: null,
                adultsInHouseNbr: null,
                childrenInHouseNbr: null,
                birthCityCd: null,
                birthCityDescTxt: null,
                birthCntryCd: null,
                birthStateCd: null,
                raceDescTxt: null,
                ethnicGroupDescTxt: null,
                versionCtrlNbr: 3,
                asOfDateAdmin: '2015-09-22T00:00:00Z',
                asOfDateEthnicity: '2015-09-22T00:00:00Z',
                asOfDateGeneral: '2015-09-22T00:00:00Z',
                asOfDateMorbidity: '2015-09-22T00:00:00Z',
                asOfDateSex: '2015-09-22T00:00:00Z',
                electronicInd: 'N',
                personParentUid: {
                    id: '10000001',
                    __typename: 'personParentUid'
                },
                dedupMatchInd: 'F',
                groupNbr: null,
                groupTime: null,
                edxInd: 'Y',
                speaksEnglishCd: null,
                additionalGenderCd: null,
                eharsId: null,
                ethnicUnkReasonCd: null,
                sexUnkReasonCd: null,
                __typename: 'Person'
            }
        ],
        total: 1,
        __typename: 'PersonResults'
    }
};
