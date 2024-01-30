import React, { RefObject, useContext, useEffect, useState } from 'react';
import { Button, ModalRef, ModalToggleButton } from '@trussworks/react-uswds';
import './AddValueset.scss';
import { ValueSetControllerService } from '../../generated';
import '../../pages/ValuesetLibrary/ValuesetTabs.scss';
import { authorization as getAuthorization } from 'authorization';
import { Concept } from '../Concept/Concept';
import { QuestionsContext } from '../../context/QuestionsContext';
import { fetchValueSet } from '../../pages/ValuesetLibrary/useValuesetAPI';

type Props = {
    modalRef?: RefObject<ModalRef>;
    updateCallback?: () => void;
    valueSetName?: string;
};

export const AddValueset = ({ modalRef, updateCallback, valueSetName }: Props) => {
    const [isLocalOrPhin, setIsLocalOrPhin] = useState('LOCAL');
    const [name, setName] = useState('');
    const [desc, setDesc] = useState('');
    const [code, setCode] = useState('');
    const [activeTab, setActiveTab] = useState('details');
    const [isValuesetNameNotValid, setIsValuesetNameNotValid] = useState(false);
    const [isValuesetCodeNotValid, setIsValuesetCodeNotValid] = useState(false);
    const [isValidationFailure, setIsValidationFailure] = useState(false);
    const { setSearchValueSet, setEditValueSet } = useContext(QuestionsContext);

    const authorization = getAuthorization();
    const handleSubmit = () => {
        const request = {
            valueSetName: name,
            valueSetCode: code,
            valueSetType: isLocalOrPhin,
            valueSetDescription: desc
        };
        ValueSetControllerService.createValueSetUsingPost({
            authorization,
            request
        }).then(({ body }) => {
            setSearchValueSet?.({ valueSetNm: body.valueSetNm, codeSetGroupId: body.codeSetGroupId });
        });
    };
    const handleUpdateValueSet = () => {
        const update = {
            valueSetNm: name,
            valueSetCode: code,
            codeSetDescTxt: desc
        };
        ValueSetControllerService.updateValueSetUsingPost({
            authorization,
            update
        }).then(({ body }) => {
            setSearchValueSet?.({ valueSetNm: body.valueSetNm, codeSetGroupId: body.codeSetGroupId });
        });
    };

    const updateQuestion = async () => {
        setDesc('');
        setCode('');
        setName('');
        updateCallback?.();
        setActiveTab('details');
    };

    const validateValuesetName = (name: string) => {
        const pattern = /^[a-zA-Z0-9]*$/;
        if (name.match(pattern)) {
            setIsValuesetNameNotValid(false);
            setIsValidationFailure(false);
        } else {
            setIsValuesetNameNotValid(true);
            setIsValidationFailure(true);
        }
    };

    const handleType = (e: any) => {
        setTimeout(() => {
            setIsLocalOrPhin(e.target.value);
        }, 10);
    };

    const validateValuesetCode = (name: string) => {
        const pattern = /^[a-zA-Z0-9_]*$/;
        if (name.match(pattern)) {
            setIsValuesetCodeNotValid(false);
            setIsValidationFailure(false);
        } else {
            setIsValuesetCodeNotValid(true);
            setIsValidationFailure(true);
        }
    };

    const handleCreateValueset = () => {
        if (valueSetName) {
            handleUpdateValueSet();
        } else {
            handleSubmit();
        }
        setActiveTab('concepts');
    };

    const reset = () => {
        if (valueSetName) return setActiveTab('concepts');
        setDesc('');
        setCode('');
        setName('');
        setSearchValueSet?.({});
        setEditValueSet?.({});
        setActiveTab('details');
    };
    const fetchDetails = async () => {
        try {
            const { content }: any = await fetchValueSet(authorization, valueSetName, '', 0, 10, {});
            setDesc(content[0].valueSetDescription);
            setCode(content[0].valueSetCode);
            setIsLocalOrPhin(content[0].type);
        } catch (error) {
            console.error('Error fetching content', error);
        }
    };
    useEffect(() => {
        if (valueSetName) {
            setName(valueSetName);
            setActiveTab('concepts');
            fetchDetails();
        }
    }, [valueSetName]);

    useEffect(() => {
        return () => {
            setActiveTab('details');
        };
    }, []);

    const disableBtn = isValidationFailure || !name || !code;

    return (
        <div className="add-valueset">
            <div className="add-valueset__content">
                <div className={`add-valueset__container ${activeTab !== 'details' ? 'concept-container' : ''}`}>
                    {activeTab === 'details' ? (
                        <div className="add-valueset__details">
                            <h3 className="main-header-title" data-testid="header-title">
                                Value set details
                            </h3>
                            <p className="description">
                                These fields will not be displayed to your users, it only makes it easier for others to
                                search for this question in the Value set library
                            </p>
                            {valueSetName ? (
                                <label>{`Value set name : ${isLocalOrPhin}`}</label>
                            ) : (
                                <>
                                    <label>
                                        All fields with <span className="mandatory-indicator">*</span> are required
                                    </label>
                                    <br></br>
                                    <input
                                        type="radio"
                                        name="isLocalOrPhin"
                                        value="LOCAL"
                                        id="rdLOCAL"
                                        className="field-space"
                                        checked={isLocalOrPhin === 'LOCAL'}
                                        onChange={handleType}
                                    />
                                    <label htmlFor="rdLOCAL" className="radio-label">
                                        LOCAL
                                    </label>
                                    <input
                                        type="radio"
                                        id="rdPHIN"
                                        name="isLocalOrPhin"
                                        value="PHIN"
                                        className="right-radio"
                                        checked={isLocalOrPhin === 'PHIN'}
                                        onChange={handleType}
                                    />
                                    <label htmlFor="rdPHIN" className="radio-label">
                                        PHIN
                                    </label>
                                </>
                            )}
                            <br />
                            {valueSetName ? (
                                <label>{`Value set name : ${valueSetName}`}</label>
                            ) : (
                                <div className={`add-valueset__input ${isValuesetCodeNotValid ? 'error-border' : ''}`}>
                                    <label>
                                        Value set code <span className="mandatory-indicator">*</span>
                                    </label>
                                    <br></br>
                                    <p data-testid="error-text" className="error-text">
                                        Value set code Not Valid
                                    </p>
                                    <input
                                        className="field-space"
                                        type="text"
                                        name="valuesetCode"
                                        maxLength={50}
                                        style={{
                                            border: isValuesetCodeNotValid ? '1px solid #dc3545' : '1px solid black'
                                        }}
                                        onBlur={(e: React.ChangeEvent<HTMLInputElement>) =>
                                            validateValuesetCode(e.target.value)
                                        }
                                        value={code}
                                        onInput={(e: React.ChangeEvent<HTMLInputElement>) => setCode(e.target.value)}
                                    />
                                </div>
                            )}
                            <div className={`add-valueset__input ${isValuesetNameNotValid ? 'error-border' : ''}`}>
                                <label>
                                    Value set name<span className="mandatory-indicator">*</span>
                                </label>
                                <p className="error-text">Value set Name Not Valid</p>
                                <input
                                    className="field-space"
                                    type="text"
                                    name="valuesetName"
                                    maxLength={50}
                                    style={{ border: isValuesetNameNotValid ? '1px solid #dc3545' : '1px solid black' }}
                                    onBlur={(e: any) => validateValuesetName(e.target.value)}
                                    value={name}
                                    onInput={(e: any) => setName(e.target.value)}
                                />
                            </div>
                            <div className="add-valueset__input">
                                <label>Value set description</label>
                                <input
                                    className="field-space"
                                    type="text"
                                    name="valuesetDesc"
                                    maxLength={200}
                                    style={{ border: '1px solid black' }}
                                    value={desc}
                                    onInput={(e: React.ChangeEvent<HTMLInputElement>) => setDesc(e.target.value)}
                                />
                            </div>
                            <div className="value-set-line-action-footer">
                                <ModalToggleButton modalRef={modalRef!} onClick={() => reset()} type="button" outline>
                                    Cancel
                                </ModalToggleButton>
                                <Button type="submit" onClick={handleCreateValueset}>
                                    {valueSetName ? 'Save changes' : 'Continue to value set concept'}
                                </Button>
                            </div>
                        </div>
                    ) : (
                        <Concept
                            onEdit={() => setActiveTab('details')}
                            valueset={{
                                valueSetTypeCd: isLocalOrPhin,
                                valueSetCode: code,
                                valueSetNm: name,
                                codeSetDescTxt: desc
                            }}
                        />
                    )}
                </div>
            </div>
            {activeTab !== 'details' ? (
                <div className="add-valueset__footer">
                    <ModalToggleButton outline modalRef={modalRef!} onClick={() => reset()} type="button" closer>
                        Cancel
                    </ModalToggleButton>
                    <ModalToggleButton
                        className="submit-btn"
                        type="button"
                        modalRef={modalRef!}
                        onClick={updateQuestion}
                        closer
                        disabled={disableBtn}>
                        Continue
                    </ModalToggleButton>
                </div>
            ) : null}
        </div>
    );
};
