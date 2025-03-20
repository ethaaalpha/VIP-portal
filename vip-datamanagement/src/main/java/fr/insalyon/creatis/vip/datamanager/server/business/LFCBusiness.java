/*
 * Copyright and authors: see LICENSE.txt in base repository.
 *
 * This software is a web portal for pipeline execution on distributed systems.
 *
 * This software is governed by the CeCILL-B license under French law and
 * abiding by the rules of distribution of free software.  You can  use,
 * modify and/ or redistribute the software under the terms of the CeCILL-B
 * license as circulated by CEA, CNRS and INRIA at the following URL
 * "http://www.cecill.info".
 *
 * As a counterpart to the access to the source code and  rights to copy,
 * modify and redistribute granted by the license, users are provided only
 * with a limited warranty  and the software's author,  the holder of the
 * economic rights,  and the successive licensors  have only  limited
 * liability.
 *
 * In this respect, the user's attention is drawn to the risks associated
 * with loading,  using,  modifying and/or developing or reproducing the
 * software by the user in light of its specific status of free software,
 * that may mean  that it is complicated to manipulate,  and  that  also
 * therefore means  that it is reserved for developers  and  experienced
 * professionals having in-depth computer knowledge. Users are therefore
 * encouraged to load and test the software's suitability as regards their
 * requirements in conditions enabling the security of their systems and/or
 * data to be ensured and,  more generally, to use and operate it in the
 * same conditions as regards security.
 *
 * The fact that you are presently reading this means that you have had
 * knowledge of the CeCILL-B license and that you accept its terms.
 */
package fr.insalyon.creatis.vip.datamanager.server.business;

import fr.insalyon.creatis.grida.client.GRIDAClient;
import fr.insalyon.creatis.grida.client.GRIDAClientException;
import fr.insalyon.creatis.grida.common.bean.GridData;
import fr.insalyon.creatis.grida.common.bean.GridPathInfo;
import fr.insalyon.creatis.vip.core.client.bean.User;
import fr.insalyon.creatis.vip.core.server.business.BusinessException;
import fr.insalyon.creatis.vip.datamanager.client.bean.Data;
import fr.insalyon.creatis.vip.datamanager.client.view.DataManagerException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Rafael Silva
 */
@Service
@Transactional
public class LFCBusiness {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private GRIDAClient gridaClient;
    private LfcPathsBusiness lfcPathsBusiness;

    @Autowired
    public LFCBusiness(GRIDAClient gridaClient, LfcPathsBusiness lfcPathsBusiness) {
        this.gridaClient = gridaClient;
        this.lfcPathsBusiness = lfcPathsBusiness;
    }

    public List<Data> listDir(User user, String baseDir, boolean refresh)
            throws BusinessException {

        try {
            List<GridData> list = gridaClient.getFolderData(
                    lfcPathsBusiness.parseBaseDir(user, baseDir), refresh);

            List<Data> dataList = new ArrayList<>();
            for (GridData data : list) {
                if (data.getType() == GridData.Type.Folder) {
                    dataList.add(new Data(data.getName(),
                            Data.Type.valueOf(data.getType().name().toLowerCase()),
                            data.getPermissions()));

                } else {
                    dataList.add(new Data(data.getName(),
                            Data.Type.valueOf(data.getType().name().toLowerCase()),
                            data.getLength(), data.getModificationDate(),
                            data.getReplicas(), data.getPermissions()));
                }
            }
            return dataList;

        } catch (DataManagerException ex) {
            throw new BusinessException(ex);
        } catch (GRIDAClientException ex) {
            logger.error("Error listing directory {} for {}", baseDir, user,ex);
            throw new BusinessException(ex);
        }
    }

    public void createDir(User user, String baseDir, String name)
            throws BusinessException {

        try {
            gridaClient.createFolder(
                    lfcPathsBusiness.parseBaseDir(user, baseDir), name);
        } catch (DataManagerException ex) {
            throw new BusinessException(ex);
        } catch (GRIDAClientException ex) {
            logger.error("Error creating directory {}/{} for {}",
                    baseDir, name, user,ex);
            throw new BusinessException(ex);
        }
    }

    public void rename(User user, String oldPath, String newPath, boolean extendPath)
            throws BusinessException {

        try {
            gridaClient.rename(
                    lfcPathsBusiness.parseBaseDir(user, oldPath),
                    lfcPathsBusiness.parseBaseDir(user, newPath));
        } catch (GRIDAClientException ex) {
            if (ex.getMessage().contains("Can not rename/move") && extendPath) {
                SimpleDateFormat sdf =
                        new SimpleDateFormat("-yyyy.MM.dd-HH.mm.ss");
                String newExtPath = newPath + sdf.format(new Date());
                rename(user, oldPath, newExtPath, false);
            } else {
                logger.error("Error renaming path {} to {} for {}",
                        oldPath, newPath, user,ex);
                throw new BusinessException(ex);
            }
        } catch (DataManagerException ex) {
            throw new BusinessException(ex);
        }
    }

    public void rename(
            User user, String baseDir, List<String> paths, String newBaseDir,
            boolean extendPath) throws BusinessException {

        for (String name : paths) {
            rename(user, baseDir + "/" + name, newBaseDir + "/" + name, extendPath);
        }
    }

    public boolean exists(User user, String path) throws BusinessException {

        try {
            return gridaClient.exist(lfcPathsBusiness.parseBaseDir(user, path));
        } catch (GRIDAClientException ex) {
            logger.error("Error checking file {} existance for {}",
                    path, user,ex);
            throw new BusinessException(ex);
        } catch (DataManagerException ex) {
            throw new BusinessException(ex);
        }
    }

    public Optional<Data.Type> getPathInfo(User user, String path) throws BusinessException {
        try {
            // convert GridPathInfo to an Optional<Data.Type> to avoid a new structure in vip.datamanager
            GridPathInfo pathInfo = gridaClient.getPathInfo(lfcPathsBusiness.parseBaseDir(user, path));
            if (pathInfo.exist()) {
                return Optional.of(Data.Type.valueOf(pathInfo.getType().name().toLowerCase()));
            } else {
                return Optional.empty();
            }
        } catch (GRIDAClientException ex) {
            logger.error("Error getting path info {} for {}", path, user, ex);
            throw new BusinessException(ex);
        } catch (DataManagerException ex) {
            throw new BusinessException(ex);
        }
    }

    public long getModificationDate(User user, String path) throws BusinessException {

        try {
            return gridaClient.getModificationDate(
                    lfcPathsBusiness.parseBaseDir(user, path));
        } catch (GRIDAClientException ex) {
            logger.error("Error getting file {} modification date for {}",
                    path, user,ex);
            throw new BusinessException(ex);
        } catch (DataManagerException ex) {
            throw new BusinessException(ex);
        }
    }

    public List<Long> getModificationDate(User user, List<String> paths)
            throws BusinessException {
        try {
            List<String> parsedPaths = new ArrayList<>();
            for (String path : paths) {
                parsedPaths.add(lfcPathsBusiness.parseBaseDir(user, path));
            }

            return gridaClient.getModificationDate(parsedPaths);
        } catch (GRIDAClientException ex) {
            logger.error("Error getting files {} modification dates for {}",
                    paths, user,ex);
            throw new BusinessException(ex);
        } catch (DataManagerException ex) {
            throw new BusinessException(ex);
        }
    }
}
