def create(COMPONENT, ENV) {
    sh 'bash create-ec2-with-env.sh ${COMPONENT} ${ENV}'
}