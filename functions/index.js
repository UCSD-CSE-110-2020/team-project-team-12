const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp();

exports.sendInviteNotifications = functions.firestore
   .document('invitations/{userId}')
   .onCreate((snap, context) => {
   const document = snap.exists ? snap.data() : null;
   var temp = context.params.userId.replace("@","");

     if (document) {
        var message = {
            notification: {
            title: 'Received a new invite',
            body: document.teamID + ' DEMANDS YOUR FRIENDSHIP'
            },
            topic: temp
            };
       return admin.messaging().send(message)
       .then((response) => {
           console.log('Successfully sent message:', response);
           return response;
           })
           .catch((error) => {
            console.log('Error sending message:', error);
            return error;
            });
     }
     return "document was null or empty";
   });



   exports.sendWalkUpdateNotifications = functions.firestore.document('schedules/{teamId}')
      .onUpdate((snap, context) => {
      //const document = snap.exists ? snap.data() : null;
      const before = snap.before.data();
      const after = snap.after.data();
      var updateFlag = 1;
      var message;
      //console.log('LOOKFORTHIS:', before.userVoteMap);
      let mapA = before.userVoteMap;
      let mapB = after.userVoteMap;
      console.warn('HEYMANLOOKHERE ' + before.isScheduled);
      console.warn('2HEYMANLOOKHERE ' + after.isScheduled);
      if (before.isScheduled===false && after.isScheduled===true){
            console.warn('3HEYMANLOOKHERE ' + snap.after.data().isScheduled);
            const temp = context.params.teamId.replace(" ","");
                 message = {
                     notification: {
                     title: 'HEY BUDDY',
                     body: 'Walkin is ascheduled'
                     },
                     topic: temp
                     };
                     //updateFlag = 1;
            }
       else if (compareMaps(mapA, mapB)){
            const temp = context.params.teamId.replace(" ","");
                 message = {
                     notification: {
                     title: 'New Vote',
                     body: 'A team member has voted'
                     },
                     topic: temp
                     };
                     //updateFlag = 1;
            }
            else
                updateFlag = 0;

        console.warn('4HEYMANLOOKHERE ' + updateFlag);
       if(updateFlag){
       return admin.messaging().send(message)
                 .then((response) => {
                     console.log('Successfully sent message:', response);
                     return response;
                     })
                     .catch((error) => {
                      console.log('Error sending message:', error);
                      return error;
                      });
                      }

             return "NEGATIVE CASE";
      });

      function compareMaps(map1, map2) {
          var testVal;
            var map1Keys = Object.getOwnPropertyNames(map1);
            var map2Keys = Object.getOwnPropertyNames(map2);
        for (var i = 0; i < map1Keys.length; i++) {
            testVal = map1Keys[i];
            //console.warn('LEWKTH ' + map1[testVal] + ' ' + map2[testVal]);
            if (map1[testVal] !== map2[testVal]) {
                return true;
                }
            }
          return false;
      }




     exports.sendWalkDeleteNotifications = functions.firestore.document('schedules/{teamId}')
      .onDelete((snap, context) => {
      //const document = snap.exists ? snap.data() : null;
      const temp = context.params.teamId.replace(" ","");
           var message = {
               notification: {
               title: 'HEY BUDDY',
               body: 'No more walkin'
               },
               topic: temp
               };

          return admin.messaging().send(message)
          .then((response) => {
              console.log('Successfully sent message:', response);
              return response;
              })
              .catch((error) => {
               console.log('Error sending message:', error);
               return error;
               });
      });

