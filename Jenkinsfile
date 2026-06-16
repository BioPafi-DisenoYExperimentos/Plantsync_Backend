pipeline {
  agent any
  tools {
    maven 'MAVEN_3_9_15'
    jdk 'JDK_21'
  }
  environment {
  //nombre de la imagen que vamos a crear para nuestra aplicación
    IMAGE_NAME = "plantsync-platform"
    TAG = "${env.BUILD_NUMBER}"
  }
  stages {
    stage ('Compile Project') {
      steps {
        withMaven(maven : 'MAVEN_3_9_15') {
            bat 'mvn clean compile'
        }
      }
    }

    /* stage('Validate Checkstyle') {
      steps {
        withMaven(maven: 'MAVEN_3_9_15') {
          bat 'mvn checkstyle:check'
        }
      }
    }
    */

    stage('Validate Unit Tests') {
      steps {
        withMaven(maven: 'MAVEN_3_9_15') {
          bat 'mvn test'
        }
      }
    }

    stage('Validate Test Coverage') {
      steps {
        withMaven(maven: 'MAVEN_3_9_15') {
          bat 'mvn clean verify jacoco:report'
          bat 'mvn jacoco:check'
        }
      }
    }

     stage ('SonarQube Analysis') {
        steps {
            withSonarQubeEnv('MiSonarServer') {
                bat 'mvn verify sonar:sonar -Dsonar.projectKey=plantsync_backend'
            }
        }
     }
    stage('Construir Imagen Docker') {
        steps {
            script {
                echo "Iniciando la construcción de la imagen Docker: ${IMAGE_NAME}:${TAG}"

                //Ejecuta el comando de Docker utilizando el socket compartido del host
                //Supone que tienes un archivo 'Dockerfile' en la raíz de tu proyecto Sprint boot
                bat "docker build -t ${IMAGE_NAME}:${TAG} ."
                bat "docker build -t ${IMAGE_NAME}:latest ."

                echo "Imagen construida exitosamente."
            }
        }
    }

    /*stage ('Package Project') {
        steps {
            withMaven(maven : 'MAVEN_3_9_15') {
                bat 'mvn package'
            }
        }
    }*/
  }
}